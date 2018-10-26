package ru.xenya.market.ui.views.admin.users;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.UserRepository;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.components.common.AbstractEditorDialog;

import java.util.List;

/*@Route(value = PAGE_USERS, layout = MainView.class)
@PageTitle(MarketConst.TITLE_USERS)*/
//@Secured(Role.ADMIN)
public class UserView extends VerticalLayout {

    private SearchBar search;

    private Grid<User> grid;

    private UserForm form;

    private UserRepository repository;

    public UserView(UserRepository repository) {
        this.repository = repository;
        form = new UserForm(this::saveUpdate, this::deleteUpdate);

        setupSearchBar();
        setupGrid();
        setupEventListeners();
    }

    private void deleteUpdate(User user) {
        repository.delete(user);
        updateList(search.getFilter());
        Notification.show("Пользователь успешно удалён", 3000, Notification.Position.BOTTOM_START);

    }

    private void saveUpdate(User user, AbstractEditorDialog.Operation operation) {
        repository.save(user);
        updateList(search.getFilter());
        Notification.show("Пользователь успешно " + operation.getNameInText(), 3000, Notification.Position.BOTTOM_START);
    }

    private void setupSearchBar() {
        search = new SearchBar();
        search.getFilterTextField().addValueChangeListener(e->updateList(search.getFilter()));
        search.setActionText("Новый пользователь");
        search.addActionClickListener(e -> openForm(new User(), AbstractEditorDialog.Operation.ADD));
        add(search);
    }

    private void setupGrid() {
        this.grid = new Grid<>();
        grid.setHeight("100vh");
        grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
        grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Имя, Фамилия").setWidth("200px").setFlexGrow(5);
        grid.addColumn(User::getRole).setHeader("Роль").setWidth("150px");
        add(grid);
        updateList(search.getFilter());
    }



    private void setupEventListeners() {
        grid.addSelectionListener(e->{
            e.getFirstSelectedItem().ifPresent(entity ->{
                //  navigateToEntity(entity.getId().toString());
                navigateToEntity(entity);
                grid.deselectAll();
            });
        });
    }

    private void navigateToEntity(String id) {
        // getUI().ifPresent(ui -> ui.navigate(TemplateUtils.generateLocation(getBasePage(), id)));
    }
    private void navigateToEntity(User user) {
        openForm(user, AbstractEditorDialog.Operation.EDIT);
    }

    private void openForm(User user, AbstractEditorDialog.Operation operation) {
        if (form.getElement().getParent() == null) {
            getUI().ifPresent(ui->ui.add(form));
        }
        form.open(user, operation);
    }

    private void updateList(String filter) {
        List<User> users = repository.findAll();
//                //findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameIgnoreCaseOrRoleLikeIgnoreCase(
//                filter, filter, filter, filter
//        );
        grid.setItems(users);
    }
}
