package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "orders")
@Data
@AllArgsConstructor
public class Order extends AbstractEntity {

  //  @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "{market.due.dueDate.required}")
    private LocalDate dueDate;

    @NotNull(message = "{market.payment.required}")
    private Payment payment;

//    //множество позиций заказа
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    @OrderColumn
//    @JoinColumn
//    @NotEmpty
//    @Valid
//    private List<OrderItem> items;
//    //множество позиций платежей
//    //счет
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_invoice_id", referencedColumnName = "invoice_id")
//    private Invoice invoice;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @OrderColumn
//    @JoinColumn
//    private List<HistoryItem> history;

    @NotNull(message = "{market.status.required}")
    private OrderState orderState;

    @NotNull
    @ManyToOne
    private Customer customer;


    public Order() {
    }

    public Order(Customer customer, User createdBy) {
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
        this.dueDate = LocalDate.now();
        setCustomer(customer);
        //this.items = new ArrayList<>();
       // addHistoryItem(createdBy, "Заказ размещён");
    }

    public Order(User createdBy){
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
        setCustomer(new Customer());
        addHistoryItem(createdBy, "Заказ размещен");
     //   this.items = new ArrayList<>();

    }

    public void addHistoryItem(User createdBy, String comment) {
//        HistoryItem item = new HistoryItem(createdBy, comment);
//        item.setNewState(orderState);
//        if (history == null) {
//            history = new LinkedList<>();
//        }
//        history.add(item);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

//    public List<OrderItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<OrderItem> items) {
//        this.items = items;
//    }
//
//    public List<HistoryItem> getHistory() {
//        return history;
//    }
//
//    public void setHistory(List<HistoryItem> history) {
//        this.history = history;
//    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void changeState(User user, OrderState orderState) {
        boolean createHistory = this.orderState != orderState && this.orderState != null && orderState != null;
        this.orderState = orderState;
        if (createHistory) {
            addHistoryItem(user, "Заказ " + orderState.name());
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "dueDate=" + dueDate.toString() +
                ", payment=" + payment.name() +
                ", orderState=" + orderState.name() +
                ", customer=" + customer.getFullName() +
                '}';
    }
}
