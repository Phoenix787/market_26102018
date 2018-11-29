package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.ComponentEvent;

public class ValueChangeEventF extends ComponentEvent<OrderItemsEditor> {
    public ValueChangeEventF(OrderItemsEditor editor) {
        super(editor, false);
    }
}
