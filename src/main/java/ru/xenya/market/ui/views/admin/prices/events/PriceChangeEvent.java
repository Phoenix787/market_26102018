package ru.xenya.market.ui.views.admin.prices.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.ui.views.admin.prices.PriceItemEditor;

public class PriceChangeEvent extends ComponentEvent<PriceItemEditor> {

    private final Service service;

    public PriceChangeEvent(PriceItemEditor source, Service service) {
        super(source, false);
        this.service = service;
    }

    public Service getService() { return service; }
}
