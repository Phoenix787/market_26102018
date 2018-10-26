package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByDueDateAfter(LocalDate filterDate);
    Page<Order> findByDueDateAfter(LocalDate filterDate, Pageable pageable);

    List<Order> findByCustomer(Customer customer);

    Page<Order> findByCustomer(Customer customer, Pageable pageable);

    Page<Order> findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate, Pageable pageable);

    List<Order> findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate);


    Page<Order> findByCustomerFullNameContainingIgnoreCase(String s, Pageable pageable);

    @Override
    Optional<Order> findById(Long id);

    long countByDueDateAfter(LocalDate date);

    long countByCustomerFullNameContainingIgnoreCase(String searchQuery);

    long countByDueDate(LocalDate date);

    long countByDueDateAndOrderStateIn(LocalDate date, Collection<OrderState> state);

    long countByOrderState(OrderState state);

    List<Order> findByCustomerAndDueDate(Customer customer, LocalDate dueDateFilter);



}
