package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.orderedit.invoice.InvoiceEditor;

public class ValueChangeEvent extends ComponentEvent<OrderItemsView> {

    public ValueChangeEvent(OrderItemsView source) {
        super(source, false);
    }
}
