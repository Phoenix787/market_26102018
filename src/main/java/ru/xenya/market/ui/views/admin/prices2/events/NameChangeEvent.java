package ru.xenya.market.ui.views.admin.prices2.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.admin.prices.PriceItemEditor;

public class NameChangeEvent extends ComponentEvent<PriceItemEditor> {

    private final String name;

    public NameChangeEvent(PriceItemEditor source, String name) {
        super(source, false);
        this.name = name;
    }

    public String getName() { return name; }
}
