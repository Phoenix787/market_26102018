package ru.xenya.market.ui.views.orderedit.invoice;

import com.vaadin.flow.component.ComponentEvent;

public class NewInvoiceEvent extends ComponentEvent<InvoiceEditor> {
    public NewInvoiceEvent(InvoiceEditor invoiceEditor) {
        super(invoiceEditor, false);
    }
}
