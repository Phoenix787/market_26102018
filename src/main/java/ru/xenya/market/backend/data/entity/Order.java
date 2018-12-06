package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "orders")
public class Order extends AbstractEntity {

    @NotNull(message = "{market.due.dueDate.required}")
    private LocalDate dueDate;

    @NotNull(message = "{market.payment.required}")
    private Payment payment;

//    //множество позиций заказа
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    @JoinColumn
//    @NotEmpty
    @Valid
    private List<OrderItem> items;
//    //множество позиций платежей

//    //счет
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Invoice invoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn
    @JoinColumn(name = "fk_oder_id")
    private List<OrderHistoryItem> history;

    @NotNull(message = "{market.status.required}")
    private OrderState orderState;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "customer_fk")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    @JoinColumn
    private Price pricePlan;


    public Order() {
    }

    public Order(Customer customer, Price price, User createdBy) {
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
        this.dueDate = LocalDate.now();
      //  this.invoice = new Invoice();
        setCustomer(customer);
        setPricePlan(price);
        this.items = new ArrayList<>();
        addHistoryItem(createdBy, "Заказ размещён");
    }

    public Order(User createdBy){
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
      //  this.invoice = new Invoice();
        setCustomer(new Customer());
        addHistoryItem(createdBy, "Заказ размещен");
        this.items = new ArrayList<>();

    }

    public void addHistoryItem(User createdBy, String comment) {
        OrderHistoryItem item = new OrderHistoryItem(createdBy, comment);
        item.setNewState(orderState);
        if (history == null) {
            history = new LinkedList<>();
        }
        history.add(item);
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
//
    public List<OrderHistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<OrderHistoryItem> history) {
        this.history = history;
    }

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

    public Invoice getInvoice() { return invoice; }

    public void setInvoice(Invoice invoice) {this.invoice = invoice; }

    public Price getPricePlan() {
        return pricePlan;
    }

    public void setPricePlan(Price pricePlan) {
        this.pricePlan = pricePlan;
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
                "dueDate=" + dueDate +
                ", payment=" + payment +
                ", items=" + items +
                ", invoice=" + invoice +
                ", orderState=" + orderState +
                ", customer=" + customer +
                ", pricePlan=" + pricePlan +
                '}';
    }

    public Integer getTotalPrice(){
        return items.stream().map(i -> i.getTotalPrice()).reduce(0, Integer::sum);
    }
}
