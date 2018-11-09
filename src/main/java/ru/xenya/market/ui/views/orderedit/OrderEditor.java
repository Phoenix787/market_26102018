package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Invoice;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.DeleteEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.utils.FormattingUtils;
import ru.xenya.market.ui.utils.TemplateUtils;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;
import ru.xenya.market.ui.views.orderedit.invoice.InvoiceEditor;
import ru.xenya.market.ui.views.orderedit.invoice.ValueChangeEvent;

import java.time.LocalDate;
import java.util.Locale;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.createItemLabelGenerator;

//todo orderitem

@Tag("order-editor")
@HtmlImport("src/views/orderedit/order-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderEditor extends PolymerTemplate<OrderEditor.Model>
        implements CrudForm<Order> /*AbstractEditorDialog<Order>*/ {


    @Id("title")
    private H2 title;
    @Id("metaContainer")
    private Div metaContainer;
    @Id("orderNumber")
    private Span orderNumber;
    @Id("status")
    private ComboBox<OrderState> status;
    @Id("dueDate")
    private DatePicker dueDate;
    @Id("payment")
    private ComboBox<Payment> payment;
    @Id("customerName")
    private TextField customerName;
    @Id("customerPhone")
    private TextField customerPhone;
    @Id("cancel")
    private Button cancel;
    @Id("save")
    private Button save;
    @Id("delete")
    private Button delete;
    @Id("itemsContainer")
    private Div itemsContainer;
    @Id("needInvoice")
    private Checkbox needInvoice;
    @Id("invoiceContainer")
    private Div invoiceContainer;
    private InvoiceEditor invoiceEditor;


//    @Id("buttons")
//    private FormButtonsBar buttons;

//    private OrderItemsEditor itemsEditor;
    private User currentUser;
    private Order currentOrder;
    private Customer currentCustomer;
    private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);
    private LocalDateToStringEncoder localDateToStringEncoder = new LocalDateToStringEncoder();

    @Autowired
    public OrderEditor() {

////        itemsEditor = new OrderItemsEditor()
////        itemsContainer.add(itemsEditor);
//        customerName.setEnabled(false);
//        customerPhone.setEnabled(false);

        invoiceEditor = new InvoiceEditor();

        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, false)));

        status.setItemLabelGenerator(createItemLabelGenerator(OrderState::toString));
        status.setDataProvider(DataProvider.ofItems(OrderState.values()));
        status.addValueChangeListener(
                e -> getModel().setStatus(DataProviderUtils.convertIfNotNull(e.getValue(), OrderState::toString)));

        binder.forField(status)
                .withValidator(new BeanValidator(Order.class, "orderState"))
                .bind(Order::getOrderState, (o, s) -> {
                    o.changeState(currentUser, s);
                });

        dueDate.setI18n(TemplateUtils.setupDatePicker());
        dueDate.setLocale(Locale.UK);
        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");

        payment.setItemLabelGenerator(createItemLabelGenerator(Payment::toString));
        payment.setDataProvider(DataProvider.ofItems(Payment.values()));
        binder.bind(payment, "payment");
        payment.setRequired(true);

        needInvoice.addValueChangeListener(e -> addInvoice(e.getValue()));
        binder.bind(invoiceEditor, "invoice");

        binder.bind(customerName, "customer.fullName");
        binder.bind(customerPhone, "customer.phoneNumbers");

        if (currentOrder != null) {
            customerName.setValue(binder.getBean().getCustomer().getFullName());
            customerPhone.setValue(binder.getBean().getCustomer().getPhoneNumbers());

            if (currentOrder.getInvoice() != null) {
                invoiceEditor.setCurrentInvoice(currentOrder.getInvoice());
            }
        }

        ComponentUtil.addListener(invoiceEditor, ValueChangeEvent.class, e -> save.setEnabled(true));
//
////        itemsEditor.setRequiredIndicatorVisible(true);
////        binder.bind(itemsEditor, "items");
//
//

    }

    private void addInvoice(Boolean value) {
        invoiceEditor.setInvoiceEnabled(value);
    }

    public void close() {

        setTotalPrice(0);
        invoiceEditor.clear();

    }

    public boolean hasChanges() {
        return binder.hasChanges();
    }

    public void write(Order order) throws ValidationException {

        binder.writeBean(order);
    }

    public void read(Order order, boolean isNew) {
        if (isNew) {
            order.setCustomer(currentCustomer);
        }
        if (order.getInvoice() != null) {
            needInvoice.setValue(true);
        }

        if (needInvoice.getValue()) {
            createInvoice(order.getInvoice());
        } else {
            createInvoice(null);
        }

        binder.setBean(order);
        System.err.println("from orderEditor->read: " + order);
        this.orderNumber.setText(isNew ? "" : order.getId().toString());

        title.setVisible(isNew);
        metaContainer.setVisible(!isNew);
        customerName.setValue(order.getCustomer().getFullName());
        customerPhone.setValue(order.getCustomer().getPhoneNumbers());


        if (order.getOrderState() != null) {
            getModel().setStatus(order.getOrderState().name());
        }

//        save.setEnabled(false);
    }

    private void createInvoice(Invoice invoice) {
        invoiceContainer.add(invoiceEditor);
        invoiceEditor.addInvoiceDateListener(e -> invoiceDateChange(invoiceEditor, e.getDate()));
        invoiceEditor.addInvoiceNumberListener(e -> invoiceNumberChange(invoiceEditor, e.getNumber()));
        invoiceEditor.setValue(invoice);
    }

    private void save() {
        Notification.show("Save click");
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }


    //public Binder<Order> getBinder() { return binder;}

//    Stream<HasValue<?, ?>> errorFields = binder.validate().getBeanValidationErrors().stream()
//                    .map(BindingValidationStatus::getField);
//    public Stream<HasValue<?, ?>> validate() {
//        return Stream.concat(errorFields, itemsEditor.validate());
    //   }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    private void setTotalPrice(int totalPrice) {
        getModel().setTotalPrice(FormattingUtils.formatAsCurrency(totalPrice));
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public FormButtonsBar getButtons() {
        return null;
    }

//    private boolean hasChanges(){
//        return getBinder().hasChanges();     // itemEditor.hasChanges();
//    }

    @Override
    public HasText getTitle() {
        return title;
    }

    @Override
    public void setBinder(BeanValidationBinder<Order> binder) {
        binder.forField(status)
                .withValidator(new BeanValidator(Order.class, "orderState"))
                .bind(Order::getOrderState, (o, s) -> {
                    o.changeState(currentUser, s);
                });
        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");
        payment.setItemLabelGenerator(createItemLabelGenerator(Payment::name));
        payment.setDataProvider(DataProvider.ofItems(Payment.values()));
        binder.bind(payment, "payment");
        payment.setRequired(true);

        binder.bind(customerName, "customer.fullName");
        binder.bind(customerPhone, "customer.phoneNumbers");

//        binder.bind(invoiceDate, "invoice.dateInvoice");
//        binder.bind(invoiceNumber, "invoice.numberInvoice");

        if (currentOrder != null) {

            customerName.setValue(binder.getBean().getCustomer().getFullName());
            customerPhone.setValue(binder.getBean().getCustomer().getPhoneNumbers());
        }
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public void clear() {
        binder.readBean(null);
        invoiceEditor.setValue(null);
        needInvoice.setValue(false);


//        itemsEditor.setValue(null);
    }

    private void invoiceDateChange(InvoiceEditor editor, LocalDate value) {
        if (editor.getCurrentInvoice() == null) {
            Invoice invoice = new Invoice();
            editor.setCurrentInvoice(invoice);
            invoice.setDateInvoice(value);
            invoice.setNumberInvoice("");
            editor.setValue(invoice);
        }
    }

    private void invoiceNumberChange(InvoiceEditor editor, String value) {
        editor.setHasChanges(true);
        editor.getCurrentInvoice().setNumberInvoice(value);
        editor.setValue(editor.getCurrentInvoice());
    }

    public interface Model extends TemplateModel {
        void setTotalPrice(String totalPrice);

        void setStatus(String status);
    }

    //    private DatePicker dueDate;
//    private ComboBox<Payment> payment;
//    private ComboBox<OrderState> orderState;
//    private Grid<OrderItem> itemGrid;
//
//    private OrderItemRepository repository;
//
//  //  private Order currentItem;
//
////    private final H3 titleField = new H3();
////    private final Button saveButton = new Button("Сохранить");
////    private final Button cancelButton = new Button("Отменить");
////    private final Button deleteButton = new Button("Удалить");
////
////    private Binder<Order> binder = new Binder<>();
////
////    private final ConfirmationDialog<Order> confirmDialog = new ConfirmationDialog<>();
//
//
//    public OrderEditor(BiConsumer<Order, Operation> itemSaver, Consumer<Order> itemDeleter) {
//        super("Заказ", itemSaver, itemDeleter);
//        this.repository = repository;
//        setTitle();
//        addNameField();
////        setupGrid();
//    }
//
//
//
//
//    private void addNameField() {
//        dueDate = new DatePicker();
//        payment = new ComboBox<>();
//        orderState = new ComboBox<>();
//    }
//
//    private void setTitle() {
//        H2 title = new H2(getBinder().getBean().getCustomer().getFullName());
//        add(title);
//    }
//
//    protected void confirmDelete() {
//        openConfirmDialog("Удалить заказ", "Вы уверены, что хотите удалить заказ?",
//                "Удаление заказа приведёт к удалению связанных с ним позиций заказа.");
//    }
//
////    public final void open(Order item, AbstractEditorDialog.Operation operation) {
////        currentItem = item;
////        titleField.setText(operation.getNameInTitle() + " " + itemType);
////        if (registrationForSave != null) {
////            registrationForSave.remove();
////        }
////        registrationForSave = saveButton.addClickListener(e -> saveClicked(operation));
////        binder.readBean(currentItem);
////
////        deleteButton.setEnabled(operation.isDeleteEnabled());
////        open();
////    }

}
