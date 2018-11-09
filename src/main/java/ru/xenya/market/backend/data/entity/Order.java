package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "orders")
@Data
@AllArgsConstructor
public class Order extends AbstractEntity {

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn/*(name = "fk_invoice_id", referencedColumnName = "invoice_id")*/
    private Invoice invoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    @JoinColumn(name = "fk_oder_id")
    private List<OrderHistoryItem> history;

    @NotNull(message = "{market.status.required}")
    private OrderState orderState;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "customer_fk")
    private Customer customer;


    public Order() {
    }

    public Order(Customer customer, User createdBy) {
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
        this.dueDate = LocalDate.now();
      //  this.invoice = new Invoice();
        setCustomer(customer);
        //this.items = new ArrayList<>();
        addHistoryItem(createdBy, "Заказ размещён");
    }

    public Order(User createdBy){
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
      //  this.invoice = new Invoice();
        setCustomer(new Customer());
        addHistoryItem(createdBy, "Заказ размещен");
     //   this.items = new ArrayList<>();

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

//    public List<OrderItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<OrderItem> items) {
//        this.items = items;
//    }
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        final Order other = (Order) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$dueDate = this.getDueDate();
        final Object other$dueDate = other.getDueDate();
        if (this$dueDate == null ? other$dueDate != null : !this$dueDate.equals(other$dueDate)) return false;
        final Object this$payment = this.getPayment();
        final Object other$payment = other.getPayment();
        if (this$payment == null ? other$payment != null : !this$payment.equals(other$payment)) return false;
        final Object this$history = this.getHistory();
        final Object other$history = other.getHistory();
        if (this$history == null ? other$history != null : !this$history.equals(other$history)) return false;
        final Object this$orderState = this.getOrderState();
        final Object other$orderState = other.getOrderState();
        if (this$orderState == null ? other$orderState != null : !this$orderState.equals(other$orderState))
            return false;
        final Object this$customer = this.getCustomer();
        final Object other$customer = other.getCustomer();
        if (this$customer == null ? other$customer != null : !this$customer.equals(other$customer)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final Object $dueDate = this.getDueDate();
        result = result * PRIME + ($dueDate == null ? 43 : $dueDate.hashCode());
        final Object $payment = this.getPayment();
        result = result * PRIME + ($payment == null ? 43 : $payment.hashCode());
        final Object $history = this.getHistory();
        result = result * PRIME + ($history == null ? 43 : $history.hashCode());
        final Object $orderState = this.getOrderState();
        result = result * PRIME + ($orderState == null ? 43 : $orderState.hashCode());
        final Object $customer = this.getCustomer();
        result = result * PRIME + ($customer == null ? 43 : $customer.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Order;
    }
}
