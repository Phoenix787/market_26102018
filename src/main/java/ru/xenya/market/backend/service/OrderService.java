package ru.xenya.market.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.OrderRepository;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;
import ru.xenya.market.ui.utils.converters.OrderStateConverter;
import ru.xenya.market.ui.utils.converters.PaymentConverter;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService implements FilterableCrudService<Order> {

    private OrderRepository orderRepository;
    private Customer currentCustomer;

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    LocalDateToStringEncoder localDateToStringEncoder = new LocalDateToStringEncoder();
    OrderStateConverter orderStateConverter = new OrderStateConverter();
    PaymentConverter paymentConverter = new PaymentConverter();

    public OrderService(OrderRepository orderRepository) {
        super();
        this.orderRepository = orderRepository;
    }

    private static final Set<OrderState> notAvailableState = Collections.unmodifiableSet(
            EnumSet.complementOf(EnumSet.of(OrderState.READY, OrderState.CONFIRMED, OrderState.CANCELED))
    );

    @Transactional (rollbackOn = Exception.class)
    public Order saveOrder(User currentUser, Long id, BiConsumer<User, Order> orderFiller) {
        Order order;
        if (id == null) {
            order = new Order(currentCustomer, currentUser);
        } else {
            order = load(id);
        }
        orderFiller.accept(currentUser, order);
        return orderRepository.save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public Order saveOrder(User currentUser, Customer currentCustomer, Long id) {
        Order order;
        if (id == null) {
            order = new Order(currentCustomer, currentUser);
        } else {
            order = load(id);
        }
        return orderRepository.save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Page<Order> findAnyMatchingAfterDueDate(Optional<String> optionalFilter,
                                                   Optional<LocalDate> optionalFilterDate,
                                                   Pageable pageable) {
        if (optionalFilter.isPresent() && !optionalFilter.get().isEmpty()) {
            if (optionalFilterDate.isPresent()) {
                return orderRepository.findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(
                        optionalFilter.get(), optionalFilterDate.get(), pageable);

            }else{
                return orderRepository.findByCustomerFullNameContainingIgnoreCase(optionalFilter.get(), pageable);
            }
        } else {
            if (optionalFilterDate.isPresent()) {
                return orderRepository.findByDueDateAfter(optionalFilterDate.get(), pageable);
            } else {
                return orderRepository.findAll(pageable);
            }
        }
    }

    @Override
    public OrderRepository getRepository() {
        return orderRepository;
    }

    @Override
    @Transactional
    public Order createNew(User currentUser) {
        Order order = new Order(currentCustomer, currentUser);
        System.out.println("from orderservice->createNew: " + order);
        order.setDueDate(LocalDate.now());
//        order.setOrderState(OrderState.NEW);
//        order.setPayment(Payment.CASH);
        return order;
    }

    @Transactional(rollbackOn = Exception.class)
    public Order addComment(User currentUser, Order order, String comment){
        order.addHistoryItem(currentUser, comment);
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()){
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByCustomerFullNameContainingIgnoreCase(
                            repositoryFilter, pageable
                    );
        } else {
            return find(pageable);
        }
    }

    private Page<Order> find(Pageable pageable) {
                   return orderRepository.findAll(pageable);
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    public List<Order> findByCustomer(Customer currentCustomer) {
        return orderRepository.findByCustomer(currentCustomer);
    }

//    public List<Order> findByCustomerAndDueDateOrOrderState(
//            Customer customer, LocalDate dueDateFilter, OrderState orderState) {
//        if (dueDateFilter != null) {
//            if (orderState != null && !orderState.name().isEmpty()) {
//                return orderRepository.findByCustomerAndDueDateOrOrderState(
//                        customer, dueDateFilter,
//                        orderState);
//
//            } else {
//                return orderRepository.findByCustomerAndDueDate(customer,
//                        dueDateFilter);
//            }
//
//        }  else if(orderState!= null && !orderState.name().isEmpty()){
//
//            return orderRepository.findByCustomerAndOrderState(customer,
//                    orderState);
//
//        }
//        return orderRepository.findByCustomer(customer);
//    }

//    public List<Order> findByCustomerAndDueDateOrOrderState(
//            Customer customer, String dueDateFilter, String orderState) {
//        if (dueDateFilter != null && !dueDateFilter.isEmpty()) {
//            if (orderState != null && !orderState.isEmpty()) {
//                return orderRepository.findByCustomerAndDueDateOrOrderState(
//                        customer, localDateToStringEncoder.decode(dueDateFilter),
//                        orderStateConverter.decode(orderState));
//
//            } else {
//                return orderRepository.findByCustomerAndDueDate(customer,
//                        localDateToStringEncoder.decode(dueDateFilter));
//            }
//
//        }  else if(dueDateFilter == null && (orderState != null && !orderState.isEmpty())){
//
//                return orderRepository.findByCustomerAndOrderState(customer,
//                                                    orderStateConverter.decode(orderState));
//
//        }
//        return orderRepository.findByCustomer(customer);
//    }


    /**
     * это используется для упрощения строки поиска в reviewList
     */
    public List<Order> findOrdersByStateOrPayment(Customer customer, String filter){
         String normalizedFilter = filter.toLowerCase();
        return findByCustomer(customer)
                .stream().filter(order -> filterTextOf(order).contains(normalizedFilter))
                .sorted(((o1, o2) -> o1.getId().compareTo(o2.getId())))
                .collect(Collectors.toList());
    }

    private String filterTextOf(Order order){
        LocalDateToStringEncoder dateConverter = new LocalDateToStringEncoder();
        String filterableText = Stream.of(order.getCustomer().getFullName(),
                dateConverter.encode(order.getDueDate()),
                orderStateConverter.encode(order.getOrderState()),
                paymentConverter.encode(order.getPayment()))
                .collect(Collectors.joining("\t"));
        return filterableText.toLowerCase();
    }

}
