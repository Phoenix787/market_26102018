package ru.xenya.market.ui.views.admin.prices.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.ui.views.admin.prices.PriceItemsEditor;

public class PriceChangeEvent extends ComponentEvent<PriceItemsEditor> {

     /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     */
    public PriceChangeEvent(PriceItemsEditor source, boolean fromClient) {
        super(source, fromClient);
    }
}
