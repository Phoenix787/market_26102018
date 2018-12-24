package ru.xenya.market.ui.views.orderedit.repayment;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.Repayment;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.views.admin.prices.utils.PriceConverter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A Designer generated component for the repayment-editor.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("repayment-editor")
@HtmlImport("src/views/orderedit/repayment/repayment-editor.html")
public class RepaymentEditor extends PolymerTemplate<RepaymentEditor.RepaymentEditorModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<RepaymentEditor, Repayment>, Repayment> {

    @Id("date")
    private DatePicker date;
    @Id("payment")
    private RadioButtonGroup<Payment> payment;
    @Id("sum")
    private TextField sum;
    @Id("cancelBtn")
    private Button cancelBtn;
    @Id("addBtn")
    private Button addBtn;

       private final AbstractFieldSupport<RepaymentEditor, Repayment> fieldSupport;
    private BeanValidationBinder<Repayment> binder = new BeanValidationBinder<>(Repayment.class);
    private boolean hasChanges = false;
    private Repayment currentRepayment;

    /**
     * Creates a new RepaymentEditor.
     */
    public RepaymentEditor() {
        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c -> {
        });

        setupPayment();

        binder.bind(date, "date");
        binder.forField(sum).withConverter(new PriceConverter())
                .bind("sum");
        binder.bind(payment, "payment");

        addBtn.addClickListener(e->{
            setValue(getValue());
           fireEvent(new SaveEvent(this, false));
        });

        cancelBtn.addClickListener(e->{
            fireEvent(new CancelEvent(this, false));
        });


    }

        private void setupPayment() {

        payment.setItems(Payment.CASH, Payment.NONCASH);
        payment.setRenderer(new TextRenderer<>(Payment::toString));
//            payment.getElement().setAttribute("theme", "vertical");
            payment.getElement().setAttribute("label", "Форма оплаты");
    }

    @Override
    public void setValue(Repayment value) {
        this.currentRepayment = value;
        fieldSupport.setValue(value);
        binder.setBean(value);
        hasChanges = false;

    }

    @Override
    public Repayment getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<RepaymentEditor, Repayment>> valueChangeListener) {
        return fieldSupport.addValueChangeListener(valueChangeListener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public void read(Repayment entity, boolean isNew) {
        if (isNew) {
            setValue(entity);
            payment.setValue(Payment.CASH);
            date.setValue(LocalDate.now());
        }
        setValue(entity);
    }

    /**
     * This model binds properties between RepaymentEditor and repayment-editor.html
     */
    public interface RepaymentEditorModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}

