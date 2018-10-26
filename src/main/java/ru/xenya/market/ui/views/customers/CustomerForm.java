//package ru.xenya.market.ui.views.customers;
//
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.validator.StringLengthValidator;
//import com.vaadin.flow.spring.annotation.SpringComponent;
//import com.vaadin.flow.spring.annotation.UIScope;
//import ru.xenya.market.backend.data.entity.Customer;
//import ru.xenya.market.ui.components.common.AbstractEditorDialog;
//
//import java.util.Objects;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//
//@SpringComponent
//@UIScope
//public class CustomerForm extends AbstractEditorDialog<Customer> {
//
//    private TextField fullName = new TextField();
//    private TextField address = new TextField();
//    private TextField phoneNumbers = new TextField();
//
//
//
//    public CustomerForm(BiConsumer<Customer, Operation> itemSaver, Consumer<Customer> itemDeleter) {
//        super("Контрагент", itemSaver, itemDeleter);
//    //    setDivEdit();
//        addNameField();
//    }
//
//    private void setDivEdit() {
//        Button editBtn = new Button("Edit");
//        Button newOrderBtn = new Button("New order");
//        Div div = new Div(editBtn, newOrderBtn);
//
//        add(div);
//    }
//
//    private void addNameField() {
//        fullName.setLabel("Наименование контрагента");
//        fullName.setRequired(true);
//        getFormLayout().add(fullName);
//        getBinder().forField(fullName).withConverter(String::trim, String::trim)
//                .withNullRepresentation("")
//                .withValidator(Objects::nonNull, "Поле должно быть заполнено")
//                .withValidator(new StringLengthValidator("Имя должно содержать 2 и более печатных символа", 2, null))
//        .bind(Customer::getFullName, Customer::setFullName);
//
//        address.setLabel("Адрес");
//        getBinder().forField(address).bind(Customer::getAddress, Customer::setAddress);
//        getFormLayout().add(address);
//        phoneNumbers.setLabel("Телефоны");
//        getBinder().forField(phoneNumbers).bind(Customer::getPhoneNumbers, Customer::setPhoneNumbers);
//        getFormLayout().add(phoneNumbers);
//    }
//
//    @Override
//    protected void confirmDelete() {
//       openConfirmDialog("Удалить контрагента",
//               "Вы уверены, что хотите удалить контрагента?", "");
//
//    }
//
//
//
//}
