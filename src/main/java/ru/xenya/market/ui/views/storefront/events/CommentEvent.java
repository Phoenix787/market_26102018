package ru.xenya.market.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.orderedit.OrderDetails;

public class CommentEvent extends ComponentEvent<OrderDetails> {


    private Long orderId;
    private String message;
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param component     the source component
     */
    public CommentEvent(OrderDetails component, Long orderId, String message) {
        super(component, false);
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }
}
