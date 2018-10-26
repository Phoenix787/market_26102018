package ru.xenya.market.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class EditEvent extends ComponentEvent<Component> {
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param //fromClient <code>true</code> if the event originated from the client
     */
    public EditEvent(Component source) {
        super(source, false);
    }
}
