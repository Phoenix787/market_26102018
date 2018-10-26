package ru.xenya.market.ui.components.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.function.Consumer;

public class ConfirmationDialog<T> extends Dialog {
    private final H3 titleField = new H3();
    private final Div messageLabel = new Div();
    private final Div extraMessageLabel = new Div();
    private final Button confirmButton = new Button();
    private final Button cancelButton = new Button("Отменить");

    private Registration registrationForConfirm;
    private Registration registrationForCancel;

    public ConfirmationDialog() {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);

        confirmButton.addClickListener(e -> close());
        confirmButton.getElement().setAttribute("theme", "tertiary");
        confirmButton.setAutofocus(true);

        cancelButton.addClickListener(e -> close());
        cancelButton.getElement().setAttribute("theme", "tertiary");

        HorizontalLayout buttonBar = new HorizontalLayout(confirmButton, cancelButton);
        Div labels = new Div(messageLabel, extraMessageLabel);
        labels.setClassName("confirm-text");

        titleField.setClassName("confirm-title");
        add(titleField, labels, buttonBar);
    }

    public void open(String title, String message, String additionalMessage, String actionName,
                     boolean isDistruptive, T item, Consumer<T> confirmHandler,
                     Runnable cancelHandler) {
        titleField.setText(title);
        messageLabel.setText(message);
        extraMessageLabel.setText(additionalMessage);
        confirmButton.setText(actionName);

        if (registrationForConfirm != null){
            registrationForConfirm.remove();
        }
        registrationForConfirm = confirmButton.addClickListener(e -> confirmHandler.accept(item));

        if (registrationForCancel != null) {
            registrationForCancel.remove();
        }
        registrationForCancel = cancelButton.addClickListener(e->cancelHandler.run());

        this.addOpenedChangeListener(e->{
            if (!e.isOpened()) {
                cancelHandler.run();
            }
        });
        if (isDistruptive) {
            confirmButton.getElement().setAttribute("theme", "tertiary error");
        }
        open();
    }

    //todo setText(message.getMessage());
    //		setHeader(message.getCaption());
    //		setCancelText(message.getCancelText());
    //		setConfirmText(message.getOkText());
    //		setOpened(true);
}
