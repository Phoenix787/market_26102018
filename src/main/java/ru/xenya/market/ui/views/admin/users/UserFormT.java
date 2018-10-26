package ru.xenya.market.ui.views.admin.users;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.Role;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;

@Tag("users-form")
@HtmlImport("src/views/admin/users/users-form.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserFormT extends PolymerTemplate<TemplateModel> implements CrudForm<User> {


    @Id("title")
    private H3 title;

    @Id("buttons")
    private FormButtonsBar buttons;

    @Id("first")
    private TextField first;
    @Id("last")
    private TextField last;
    @Id("email")
    private TextField email;
    @Id("password")
    private PasswordField password;
    @Id("role")
    private ComboBox<String> role;

//    private final PasswordEncoder passwordEncoder;


 //   @Autowired
    public UserFormT() {
    }

    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    @Override
    public HasText getTitle() {
        return title;
    }

    @Override
    public void setBinder(BeanValidationBinder<User> binder) {
        ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getRoles());
        role.setItemLabelGenerator(s -> s != null ? s : "");
        role.setDataProvider(roleProvider);

        binder.bind(first, "firstName");
        binder.bind(last, "lastName");
        binder.bind(email, "email");
        binder.bind(role, "role");
        binder.bind(password, "passwordHash");
//        binder.forField(password).withValidator(pass -> {
//            return pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$");
//        }, "need 6 or more chars, mixing digits, lowercase and uppercase letters")
//                .bind(user -> password.getEmptyValue(), (user, pass) -> {
//                    if (!password.getEmptyValue().equals(pass)) {
//                        user.setPasswordHash(passwordEncoder.encode(pass));
//                    }
//                });

    }
}
