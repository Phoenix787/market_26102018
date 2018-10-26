package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
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
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.utils.FormattingUtils;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;

//todo

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.createItemLabelGenerator;

@Tag("order-editor")
@HtmlImport("src/views/orderedit/order-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderEditor extends PolymerTemplate<OrderEditor.Model>
        implements CrudForm<Order> /*AbstractEditorDialog<Order>*/ {




    public interface Model extends TemplateModel{
        void setTotalPrice(String totalPrice);

        void setStatus(String status);
    }

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
    private Button review;
    @Id("itemsContainer")
    private Div itemsContainer;

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
////        itemsContainer.addClassName(itemsEditor);
//        customerName.setEnabled(false);
//        customerPhone.setEnabled(false);
        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        review.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        status.setItemLabelGenerator(createItemLabelGenerator(OrderState::getDisplayName));
        status.setDataProvider(DataProvider.ofItems(OrderState.values()));
        status.addValueChangeListener(
                e->getModel().setStatus(DataProviderUtils.convertIfNotNull(e.getValue(), OrderState::toString)));

        binder.forField(status)
                .withValidator(new BeanValidator(Order.class, "orderState"))
                .bind(Order::getOrderState, (o, s)->{
                    o.changeState(currentUser, s);
                });
        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");
//        //todo для поля дата установить валидатор
//
        payment.setItemLabelGenerator(createItemLabelGenerator(Payment::toString));
        payment.setDataProvider(DataProvider.ofItems(Payment.values()));
        binder.bind(payment, "payment");
        payment.setRequired(true);

        binder.bind(customerName, "customer.fullName");
        binder.bind(customerPhone, "customer.phoneNumbers");

        if (currentOrder != null) {

            customerName.setValue(binder.getBean().getCustomer().getFullName());
            customerPhone.setValue(binder.getBean().getCustomer().getPhoneNumbers());
        }

//
////        itemsEditor.setRequiredIndicatorVisible(true);
////        binder.bind(itemsEditor, "items");
//
//

    }


    public void close(){
        setTotalPrice(0);
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

        binder.setBean(order);
        System.err.println("from orderEditor->read: " + order);
      //  binder.readBean(order);
        this.orderNumber.setText(isNew ? "" : order.getId().toString());

        title.setVisible(isNew);
        metaContainer.setVisible(!isNew);
        customerName.setValue(order.getCustomer().getFullName());
        customerPhone.setValue(order.getCustomer().getPhoneNumbers());

        if (order.getOrderState() != null) {
            getModel().setStatus(order.getOrderState().name());
        }

//        review.setEnabled(false);
    }

    private void save(){
        Notification.show("Save click");
    }


    //public Binder<Order> getBinder() { return binder;}

//    Stream<HasValue<?, ?>> errorFields = binder.validate().getBeanValidationErrors().stream()
//                    .map(BindingValidationStatus::getField);
//    public Stream<HasValue<?, ?>> validate() {
//        return Stream.concat(errorFields, itemsEditor.validate());
 //   }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
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

//    private boolean hasChanges(){
//        return getBinder().hasChanges();     // itemEditor.hasChanges();
//    }

    @Override
    public FormButtonsBar getButtons() {
        return null;
    }

    @Override
    public HasText getTitle() {
        return title;
    }

    @Override
    public void setBinder(BeanValidationBinder<Order> binder) {
       binder.forField(status)
                .withValidator(new BeanValidator(Order.class, "orderState"))
                .bind(Order::getOrderState, (o, s)->{
                    o.changeState(currentUser, s);
                });
        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");
//        //todo для поля дата установить валидатор
//
        payment.setItemLabelGenerator(createItemLabelGenerator(Payment::name));
        payment.setDataProvider(DataProvider.ofItems(Payment.values()));
        binder.bind(payment, "payment");
        payment.setRequired(true);

        binder.bind(customerName, "customer.fullName");
        binder.bind(customerPhone, "customer.phoneNumbers");

        if (currentOrder != null) {

            customerName.setValue(binder.getBean().getCustomer().getFullName());
            customerPhone.setValue(binder.getBean().getCustomer().getPhoneNumbers());
        }
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;

       // customerName.setValue(currentCustomer.getFullName());
    }

    public void clear() {
        binder.readBean(null);
//        itemsEditor.setValue(null);
    }

//    public boolean hasChanges() {
//        return binder.hasChanges() /*|| itemsEditor.hasChanges()*/;
//    }




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
