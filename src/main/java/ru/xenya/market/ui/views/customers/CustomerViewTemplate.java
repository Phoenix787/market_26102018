package ru.xenya.market.ui.views.customers;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.components.common.ConfirmationDialog;
import ru.xenya.market.ui.crud.CrudEntityPresenter;
import ru.xenya.market.ui.crud.CrudView;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.views.EntityView;
import ru.xenya.market.ui.views.orderedit.OrdersViewOfCustomer;

import static ru.xenya.market.ui.utils.MarketConst.PAGE_CUSTOMERS;

@Tag("customer-view")
@HtmlImport("src/views/customer/customer-view.html")
@Route(value = PAGE_CUSTOMERS, layout = MainView.class)
@PageTitle(MarketConst.TITLE_CUSTOMERS)
public class CustomerViewTemplate extends CrudView<Customer, TemplateModel>/**/ {

    @Id("search")
    private SearchBar search;

    @Id("grid")
    private Grid<Customer> grid;

    //в этом диалоге можно попробывать открывать заказы текущего клиента
    @Id("dialog")
    private Dialog dialog;

    private ConfirmationDialog<Customer> confirmation;


    private final CrudEntityPresenter<Customer> customerPresenter;

    private final OrdersViewOfCustomer ordersView;

    private final BeanValidationBinder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    @Autowired
    public CustomerViewTemplate(CrudEntityPresenter<Customer> customerPresenter, CustomerEditor customerEditor
                                , OrdersViewOfCustomer ordersView) {
        super(EntityUtil.getName(Customer.class), customerEditor);
        this.customerPresenter = customerPresenter;
        this.ordersView = ordersView;
//        this.customerEditor = customerEditor;

        customerEditor.setBinder(binder);
        setupEventListeners();
        setupGrid();
        customerPresenter.setView(this);
        //dialog.add(this.ordersView);

        dialog.getElement().addEventListener("opened-changed", e -> {
            if (!dialog.isOpened()) {
                // Handle client-side closing dialog on escape
                customerPresenter.cancel();
            }
        });

    }

    private void setupGrid() {
        grid.setHeight("100vh");
        grid.addColumn(Customer::getId).setWidth("50px").setFlexGrow(0);
        grid.addColumn(Customer::getFullName).setWidth("250px").setHeader("Наименование").setFlexGrow(5);
        grid.addColumn(Customer::getAddress).setWidth("200px").setHeader("Адрес").setFlexGrow(5);
        grid.addColumn(Customer::getPhoneNumbers).setWidth("250px").setFlexGrow(5);
//        grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setFlexGrow(2);
        grid.addColumn(new ComponentRenderer<>(this::openOrdersButton)).setFlexGrow(2);
    }

    private Button openOrdersButton(Customer customer) {
        Button button = new Button("Заказы", VaadinIcon.CHECK.create());
        button.addClickListener(e -> {
            ordersView.open(customer);
          //  getUI().ifPresent(ui -> ui.navigate(MarketConst.PAGE_STOREFRONT));
            dialog.add(ordersView);
           dialog.open();
        });
        return button;
    }

    @Override
    protected CrudEntityPresenter<Customer> getPresenter() {
        return customerPresenter;
    }

    @Override
    protected String getBasePage() {
        return PAGE_CUSTOMERS;
    }

    @Override
    protected BeanValidationBinder<Customer> getBinder() {
        return binder;
    }

    @Override
    protected SearchBar getSearchBar() {
        return search;
    }

    @Override
    public Grid<Customer> getGrid() {
        return grid;
    }
}