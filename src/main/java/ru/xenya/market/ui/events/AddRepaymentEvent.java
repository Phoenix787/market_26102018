package ru.xenya.market.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.orderedit.OrderEditor;

public class AddRepaymentEvent extends ComponentEvent<OrderEditor> {

    public AddRepaymentEvent(OrderEditor source, boolean fromClient) {
        super(source, fromClient);
    }
}
