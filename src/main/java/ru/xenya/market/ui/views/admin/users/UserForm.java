package ru.xenya.market.ui.views.admin.users;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.xenya.market.backend.data.Role;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.components.common.AbstractEditorDialog;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

//@SpringComponent
//@UIScope
public class UserForm extends AbstractEditorDialog<User> {

    private H3 title;

    private TextField email;

    private TextField first;
    private TextField last;
    private PasswordField password;
    private ComboBox<String> role;

  //  private final PasswordEncoder passwordEncoder;



    public UserForm(BiConsumer<User, Operation> itemSaver, Consumer<User> itemDeleter) {
        super("Пользователь", itemSaver, itemDeleter);
        addFields();
    }

    @Override
    protected void confirmDelete() {

    }

    private void addFields() {
        email = new TextField();
        role = new ComboBox<>();
        first = new TextField();
        last = new TextField();
        password = new PasswordField();

        ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getRoles());
        role.setItemLabelGenerator(s->s != null ? s : "");
        role.setDataProvider(roleProvider);

        title = new H3("MMarket");

        email.setTitle("Email(логин)");
        email.setRequired(true);
        getBinder().forField(email).bind(User::getEmail, User::setEmail);
        getBinder().forField(first).bind(User::getFirstName, User::setFirstName);
        getBinder().forField(last).bind(User::getLastName, User::setLastName);
        getBinder().forField(role).bind(User::getRole, User::setRole);

        getBinder().forField(password).withValidator(pass->{
            return pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$");
        }, "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(user->password.getEmptyValue(), (user, pass)->{
                    if (!password.getEmptyValue().equals(pass)){
//                        users.setPasswordHash(passwordEncoder.encode(pass));
                        user.setPasswordHash(pass);
                    }
                });
        first.setTitle("Имя");
        last.setTitle("Фамилия");
        password.setTitle("Пароль");
        role.setLabel("Роль");
        getFormLayout().add(email, first, last, password, role);

    }
}
