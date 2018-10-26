package ru.xenya.market.ui.views.customers;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;

@Tag("customer-editor")
@HtmlImport("src/views/customer/customer-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerEditor extends PolymerTemplate<TemplateModel> implements CrudForm<Customer> {

    @Id("title")
    private H3 title;

    @Id("buttons")
    private FormButtonsBar buttons;

    @Id("fullName")
    private TextField fullName;
    @Id("address")
    private TextField address;
    @Id("phoneNumbers")
    private TextField phoneNumbers;

//    @Id("ordersContainer")
//    private Div ordersContainer;



    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    @Override
    public HasText getTitle() {
        return title;
    }

    @Override
    public void setBinder(BeanValidationBinder<Customer> binder) {
        binder.bind(fullName, "fullName");
        binder.bind(address, "address");
        binder.bind(phoneNumbers, "phoneNumbers");
//
    }
}
