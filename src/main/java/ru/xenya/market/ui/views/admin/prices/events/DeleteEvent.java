package ru.xenya.market.ui.views.admin.prices.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.admin.prices.PriceItemEditor;

public class DeleteEvent extends ComponentEvent<PriceItemEditor> {

    public DeleteEvent(PriceItemEditor source) {
        super(source, false);
    }
}
