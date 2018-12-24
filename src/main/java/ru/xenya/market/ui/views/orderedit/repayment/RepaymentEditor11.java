//package ru.xenya.market.ui.views.orderedit.repayment;
//
//import com.vaadin.flow.component.AbstractField;
//import com.vaadin.flow.component.ComponentEventListener;
//import com.vaadin.flow.component.HasValueAndElement;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.html.H4;
//import com.vaadin.flow.component.html.Label;
//import com.vaadin.flow.component.internal.AbstractFieldSupport;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.renderer.TextRenderer;
//import com.vaadin.flow.shared.Registration;
//import ru.xenya.market.backend.data.Payment;
//import ru.xenya.market.backend.data.entity.Repayment;
//import ru.xenya.market.ui.events.CancelEvent;
//import ru.xenya.market.ui.events.SaveEvent;
//import ru.xenya.market.ui.views.admin.prices.utils.PriceConverter;
//
//import java.util.Objects;
//
//public class RepaymentEditor11 extends VerticalLayout
//        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<RepaymentEditor, Repayment>, Repayment> {
//
//   private DatePicker datePicker;
//   private TextField summ;
//   private RadioButtonGroup<Payment> payment;
//
//   private Button addButton;
//   private Button cancelButton;
//
//   private final AbstractFieldSupport<RepaymentEditor, Repayment> fieldSupport;
//    private BeanValidationBinder<Repayment> binder = new BeanValidationBinder<>(Repayment.class);
//    private boolean hasChanges = false;
//    private Repayment currentRepayment;
//
//
//    public RepaymentEditor() {
//
//        this.fieldSupport = new AbstractFieldSupport<>(this, null,
//                Objects::equals, c -> {
//        });
//        datePicker = new DatePicker("Дата платежа");
//        summ = new TextField("Сумма");
//        payment = new RadioButtonGroup<>();
//        payment.setId("payment");
//
//        H4 title = new H4("Платежи");
//
//        setupPayment();
//
//        addButton = new Button("Сохранить");
//        cancelButton = new Button("Отменить");
//
//        HorizontalLayout main = new HorizontalLayout();
//        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, addButton);
//        main.add(new VerticalLayout(datePicker, summ), payment);
//
//        binder.bind(datePicker, "date");
//        binder.forField(summ).withConverter(new PriceConverter())
//                .bind("sum");
//        binder.bind(payment, "payment");
//
//
//        addButton.getElement().setAttribute("theme", "primary");
//        addButton.addClickListener(e->{
//            setValue(getValue());
//           fireEvent(new SaveEvent(this, false));
//        });
//
//        cancelButton.addClickListener(e->{
//            fireEvent(new CancelEvent(this, false));
//        });
//
//        Label paymentLabel = new Label("Форма оплаты");
//        paymentLabel.setFor(payment);
//        paymentLabel.getElement().setProperty("slot", "label");
////        add(new Div(title), main, buttonLayout);
//        add(new Div(title), datePicker, paymentLabel, payment, summ, buttonLayout);
//    }
//
//    private void setupPayment() {
//        payment.setItems(Payment.CASH, Payment.NONCASH);
//        payment.setValue(Payment.CASH);
//        payment.setRenderer(new TextRenderer<>(Payment::toString));
//    }
//
//    @Override
//    public void setValue(Repayment value) {
//        this.currentRepayment = value;
//        fieldSupport.setValue(value);
//        binder.setBean(value);
//        hasChanges = false;
//
//    }
//
//    @Override
//    public Repayment getValue() {
//        return fieldSupport.getValue();
//    }
//
//    @Override
//    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<RepaymentEditor, Repayment>> valueChangeListener) {
//        return fieldSupport.addValueChangeListener(valueChangeListener);
//    }
//
//    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
//        return addListener(SaveEvent.class, listener);
//    }
//
//    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
//        return addListener(CancelEvent.class, listener);
//    }
//}
