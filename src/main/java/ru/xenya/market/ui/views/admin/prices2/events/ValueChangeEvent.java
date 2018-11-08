package ru.xenya.market.ui.views.admin.prices2.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.admin.prices.PriceItemsEditor;

public class ValueChangeEvent extends ComponentEvent<PriceItemsEditor> {

    public ValueChangeEvent(PriceItemsEditor source) {
        super(source, false);
    }
}
