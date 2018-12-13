package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.ComponentEvent;


public class TotalPriceEvent extends ComponentEvent<OrderItemsView> {
    private final Integer totalPrice;
    public TotalPriceEvent(OrderItemsView component, Integer totalPrice) {
        super(component, false);
        this.totalPrice = totalPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
