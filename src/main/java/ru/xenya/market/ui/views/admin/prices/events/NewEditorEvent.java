package ru.xenya.market.ui.views.admin.prices.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.admin.prices.PriceItemsEditor;

public class NewEditorEvent extends ComponentEvent<PriceItemsEditor> {
    public NewEditorEvent(PriceItemsEditor component) {
        super(component, false);
    }
}
