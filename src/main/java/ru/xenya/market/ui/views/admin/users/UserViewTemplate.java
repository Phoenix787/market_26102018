package ru.xenya.market.ui.views.admin.users;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.crud.CrudEntityPresenter;
import ru.xenya.market.ui.crud.CrudView;
import ru.xenya.market.ui.utils.MarketConst;

import static ru.xenya.market.ui.utils.MarketConst.PAGE_USERS;

@Tag("users-view")
@HtmlImport("src/views/admin/users/users-view.html")
@Route(value = PAGE_USERS, layout = MainView.class)
@PageTitle(MarketConst.TITLE_USERS)
public class UserViewTemplate extends CrudView<User, TemplateModel> {

    @Id("search")
    private SearchBar search;

    @Id("grid")
    private Grid<User> grid;

    private final CrudEntityPresenter<User> presenter;

    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);


    @Autowired
    public UserViewTemplate(CrudEntityPresenter<User> presenter, UserFormT form) {
        super(EntityUtil.getName(User.class), form);
        this.presenter = presenter;

        form.setBinder(binder);

        setupEventListeners();
        setupGrid();
        presenter.setView(this);
    }

    private void setupGrid() {
        grid.setWidth("100vh");
        grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
        grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Имя, Фамилия").setWidth("200px").setFlexGrow(5);
        grid.addColumn(User::getRole).setHeader("Роль").setWidth("150px");
    }


    @Override
    protected CrudEntityPresenter<User> getPresenter() {
        return presenter;
    }

    @Override
    protected String getBasePage() {
        return PAGE_USERS;
    }

    @Override
    protected BeanValidationBinder<User> getBinder() {
        return binder;
    }

    @Override
    protected SearchBar getSearchBar() {
        return search;
    }

    @Override
    protected Grid<User> getGrid() {
        return grid;
    }
}
