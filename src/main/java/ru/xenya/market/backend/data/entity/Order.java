package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.util.OrderItemSummary;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@NamedEntityGraphs({@NamedEntityGraph(name = Order.ENTITY_GRAPTH_BRIEF, attributeNodes = {
        @NamedAttributeNode("customer"),
        @NamedAttributeNode(value = "items", subgraph = "itemsGraph")
},
        subgraphs = {
                @NamedSubgraph(name = "itemsGraph",attributeNodes = {
                        @NamedAttributeNode(value = "dates")
                })
        }
),@NamedEntityGraph(name = Order.ENTITY_GRAPTH_FULL, attributeNodes = {
        @NamedAttributeNode("customer"),
        @NamedAttributeNode("invoice"),
        @NamedAttributeNode(value = "pricePlan", subgraph = "priceGraph"),
        @NamedAttributeNode("history"),
        @NamedAttributeNode(value = "items", subgraph = "itemsGraph")
},
            subgraphs = {
                    @NamedSubgraph(name = "itemsGraph",attributeNodes = {
                            @NamedAttributeNode(value = "dates")
                    }),
                    @NamedSubgraph(name = "priceGraph",attributeNodes = {
                            @NamedAttributeNode(value = "itemsPrice")
                    })

            }
)})
@Entity(name = "orders")
public class Order extends AbstractEntity implements OrderSummary {


    public static final String ENTITY_GRAPTH_BRIEF = "Order.brief";
    public static final String ENTITY_GRAPTH_FULL = "Order.full";


    @NotNull(message = "{market.due.dueDate.required}")
    private LocalDate dueDate;

    @NotNull(message = "{market.payment.required}")
    private Payment payment;

   //множество позиций заказа
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true )
    @OrderColumn(nullable = false)
    @JoinColumn
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @Valid
    private List<OrderItem> items;

    //множество позиций платежей
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn(/*nullable = false*/)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 100)
    @JoinColumn
    private List<Repayment> repayments;

    //счет
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn
    private Invoice invoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
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
        this.repayments = new ArrayList<>();
        addHistoryItem(createdBy, "Заказ размещён");
    }

    public Order(User createdBy){
        this.orderState = OrderState.NEW;
        this.payment = Payment.CASH;
      //  this.invoice = new Invoice();
        setCustomer(new Customer());
        addHistoryItem(createdBy, "Заказ размещен");
        this.items = new ArrayList<>();
        this.repayments = new ArrayList<>();


    }

    public void addHistoryItem(User createdBy, String comment) {
        OrderHistoryItem item = new OrderHistoryItem(createdBy, comment);
        item.setNewState(orderState);
        if (history == null) {
            history = new LinkedList<>();
        }
        history.add(item);
    }

    public void addRepayment(Repayment repayment) {
        if (repayments == null) {
            repayments = new ArrayList<>();
        }
        repayments.add(repayment);
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

    @Override
    public List<OrderItem> getItems() {
        return items;
    }

//    public List<OrderItemSummary> getItemsSummary(){
//        return new ArrayList<>(items);
//    }

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

    @Override
    public OrderState getState() {
        return getOrderState();
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

    public List<Repayment> getRepayments() {
        return repayments;
    }

    public void setRepayments(List<Repayment> repayments) {
        this.repayments = repayments;
    }

    public void changeState(User user, OrderState orderState) {
        boolean createHistory = this.orderState != orderState && this.orderState != null && orderState != null;
        this.orderState = orderState;
        if (createHistory) {
            addHistoryItem(user, "Заказ " + orderState.toString());
        }
    }

    @Override
    public String toString() {
        return "Order{" +  "dueDate=" + dueDate + ", payment=" + payment +
                /*", items=" + items +*/ ", invoice=" + invoice +  ", orderState=" + orderState +
                ", customer=" + customer + ", pricePlan=" + pricePlan + '}';
    }

    public Integer getTotalPrice(){
        return items.stream().map(i -> i.getTotalPrice()).reduce(0, Integer::sum);
    }
    public Integer getPaysTotalPrice(){
        return repayments.stream().map(Repayment::getSum).reduce(0, Integer::sum);
    }

}
