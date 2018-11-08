package ru.xenya.market.ui.views.orderedit.invoice;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.backend.data.entity.Invoice;
import ru.xenya.market.ui.utils.TemplateUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;


@Tag("invoice-editor")
@HtmlImport("src/views/orderedit/invoice/invoice-editor.html")
public class InvoiceEditor extends PolymerTemplate<TemplateModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<InvoiceEditor, Invoice>, Invoice> {

    //    @Id("check")
//    private Checkbox check;
    @Id("invoiceDate")
    private DatePicker invoiceDate;

    @Id("invoiceNumber")
    private TextField invoiceNumber;

    private Invoice currentInvoice;

    private boolean hasChanges = false;

    private final AbstractFieldSupport<InvoiceEditor, Invoice> fieldSupport;

    private BeanValidationBinder<Invoice> binder = new BeanValidationBinder<>(Invoice.class);

    public InvoiceEditor() {
        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c->{} );

     //   check.addValueChangeListener(e -> addInvoice(e.getValue()));

        invoiceDate.addValueChangeListener(e ->
                fireEvent(new InvoiceDateEvent(this, e.getValue())));

        invoiceDate.setI18n(TemplateUtils.setupDatePicker());
        invoiceDate.setLocale(Locale.UK);
        binder.bind(invoiceDate, "dateInvoice");

        invoiceNumber.addValueChangeListener(e ->
                fireEvent(new InvoiceNumberEvent(this, e.getValue())));

        binder.bind(invoiceNumber, "numberInvoice");
    }

//    private void invoiceNumberChange(String value) {
//        setHasChanges(true);
//        currentInvoice.setNumberInvoice(value);
//        binder.setBean(currentInvoice);
//        setValue(currentInvoice);
//        fireEvent(new NewInvoiceEvent(this));
//    }

//    private void invoiceDateChange(LocalDate value) {
//        System.err.println(value == null ? "-----date value = null" : "date value not null");
//        if (check.getValue() && value == null) {
//            Invoice invoice = new Invoice();
//            this.currentInvoice = invoice;
//            invoice.setDateInvoice(value);
//            binder.setBean(invoice);
//            setValue(invoice);
//            fireEvent(new NewInvoiceEvent(this));
//        } else if (check.getValue() && currentInvoice != null){
//            binder.setBean(currentInvoice);
//            setValue(currentInvoice);
//            fireEvent(new NewInvoiceEvent(this));
//        }
//
//    }

    @Override
    public void setValue(Invoice value) {
        System.err.println(value == null ? "--------value = null" : "----------value not null");

        fieldSupport.setValue(value);
        binder.setBean(value);
        boolean noDateSelected = value == null || value.getDateInvoice() == null;
        invoiceNumber.setEnabled(!noDateSelected);
    }


    public boolean isHasChanges() {
        return hasChanges;
    }

    public void setHasChanges(boolean hasChanges){
        this.hasChanges = hasChanges;
        if (hasChanges){
            fireEvent(new ru.xenya.market.ui.views.orderedit.invoice.ValueChangeEvent(this));
        }
    }
    @Override
    public Invoice getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<InvoiceEditor, Invoice>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    public void setCurrentInvoice(Invoice invoice) {
        this.currentInvoice = invoice;
    }

    public void setInvoiceEnabled(Boolean value) {
        invoiceDate.setEnabled(value);
        //invoiceNumber.setEnabled(value);
    }

    public Invoice getCurrentInvoice() {
        return currentInvoice;
    }

    public Binder<Invoice> getBinder() {
        return binder;
    }

    public class InvoiceDateEvent extends ComponentEvent<InvoiceEditor> {
        private final LocalDate date;

        public InvoiceDateEvent(InvoiceEditor invoiceEditor, LocalDate value) {
            super(invoiceEditor, false);
            this.date = value;
        }

        public LocalDate getDate() {
            return date;
        }
    }

    public Registration addInvoiceDateListener(ComponentEventListener<InvoiceDateEvent> listener) {
        return addListener(InvoiceDateEvent.class, listener);
    }

    public class InvoiceNumberEvent extends ComponentEvent<InvoiceEditor> {

        private final String number;

        public InvoiceNumberEvent(InvoiceEditor invoiceEditor, String value) {
            super(invoiceEditor, false);
            number = value;
        }

        public String getNumber() {      return number;    }
    }

    public Registration addInvoiceNumberListener(ComponentEventListener<InvoiceNumberEvent> listener) {
        return addListener(InvoiceNumberEvent.class, listener);
    }

    public void clear() {
      //  check.setValue(false);
    }


}
