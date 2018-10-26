package ru.xenya.market.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.orderedit.OrderEditor;

public class ReviewEvent extends ComponentEvent<OrderEditor> {
    public ReviewEvent(OrderEditor component) {
        super(component, false);
    }
}
