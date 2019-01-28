package ru.xenya.market.backend.data.entity;

import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.entity.util.OrderItemSummary;

import java.time.LocalDate;
import java.util.List;

public interface OrderSummary {
    Long getId();

    OrderState getState();

    Customer getCustomer();

    List<OrderItem> getItems();

    LocalDate getDueDate();

    Integer getTotalPrice();
}
