package ru.xenya.market.ui.views.orderedit.invoice;

import com.vaadin.flow.component.ComponentEvent;

public class ValueChangeEvent extends ComponentEvent<InvoiceEditor> {

    public ValueChangeEvent(InvoiceEditor source) {
        super(source, false);
    }
}
