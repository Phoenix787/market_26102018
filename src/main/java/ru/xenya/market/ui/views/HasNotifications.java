package ru.xenya.market.ui.views;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import ru.xenya.market.ui.utils.MarketConst;

public interface HasNotifications extends HasElement {

    default void showNotification(String message) {showNotification(message, false);}

    default void showNotification(String message, boolean persistent){
        if (persistent){
            Button close = new Button("Закрыть");
            close.getElement().setAttribute("theme", "tertiary small error");
            Notification notification = new Notification(new Text(message), close);
            notification.setPosition(Notification.Position.BOTTOM_START);
            notification.setDuration(0);
            close.addClickListener(event -> notification.close());
            notification.open();
        } else {
            Notification.show(message, MarketConst.NOTIFICATION_DURATION, Notification.Position.BOTTOM_STRETCH);
        }
    }
}
