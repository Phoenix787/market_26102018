package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.crud.CrudEntityPresenter;
import ru.xenya.market.ui.crud.CrudView;
import ru.xenya.market.ui.crud.OrderPresenter;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.utils.TemplateUtils;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;
import ru.xenya.market.ui.utils.converters.LongToStringEncoder;
import ru.xenya.market.ui.utils.converters.OrderStateConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

//todo попробовать сделать страницу похожую на reviewList в buddyApp

@Tag("orders-view-of-customer")
@Route(value = MarketConst.PAGE_STOREFRONT, layout = MainView.class)
@HtmlImport("src/views/orderedit/orders-view-of-customer.html")
@SpringComponent
@UIScope
public class OrdersViewOfCustomer extends CrudView<Order, TemplateModel> {

    @Id("text")
    private TextField text;

    @Id("search")
    private SearchBar searchBar;

    @Id("grid")
    private Grid<Order> grid;

    //private Dialog dialog;
    private Customer currentCustomer;


//    private final CrudEntityPresenter<Order> presenter;

    private final OrderPresenter presenter;

//    private OrderEditor form;

  //  private final BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

    @Autowired
    public OrdersViewOfCustomer(OrderPresenter presenter, OrderEditor form) {
        super(EntityUtil.getName(Order.class), form);
        this.presenter = presenter;
      //  this.form = form;
//        presenter.setView(this);
        presenter.init(this);
        setupGrid();
        setupEventListeners();
      //  form.setBinder(binder);
    }


    public void open(Customer customer) {
        text.setValue(customer.getFullName());
        presenter.setCurrentCustomer(customer);
        grid.setItems(presenter.updateList());
    }

    private void setupGrid() {
        LocalDateToStringEncoder dateConverter = new LocalDateToStringEncoder();
        grid.setHeight("100vh");
        grid.addColumn(Order::getId).setWidth("50px").setFlexGrow(0);
        grid.addColumn(new LocalDateRenderer<>(Order::getDueDate,
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Дата заказа").setFlexGrow(5);
        grid.addColumn(Order::getOrderState).setWidth("150px").setHeader("Статус").setFlexGrow(5);
        grid.addColumn(Order::getPayment).setWidth("150px").setHeader("Форма оплаты").setFlexGrow(5);
//        grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setFlexGrow(2);
//        grid.addColumn(new ComponentRenderer<>(this::openOrdersButton)).setFlexGrow(2);
    }

    public void setupEventListeners() {
        getGrid().addSelectionListener(e->{
            e.getFirstSelectedItem().ifPresent(entity->{
                System.err.println(entity);

                presenter.onNavigation(entity.getId(), true); //load(entity);
                //UI.getCurrent().navigate(MarketConst.PAGE_STOREFRONT + "/" + entity.getId());
//                getPresenter().load(entity);
                //navigateToEntity(entity.getId().toString());
                getGrid().deselectAll();
            });
        });

        //   getForm().getButtons().addSaveListener(e -> getPresenter().save());
        //   getForm().getButtons().addCancelListener(e -> getPresenter().cancel());

        getDialog().getElement().addEventListener("opened-changed", e->{
            if (!getDialog().isOpened()) {
                //  getPresenter().cancel();
            }
        });

//        getForm().getButtons().addDeleteListener(e -> getPresenter().delete());

       getSearchBar().addActionClickListener(e -> getPresenter().createNewOrder());
       getSearchBar().addFilterChangeListener(e->getPresenter().filter(getSearchBar().getFilter()));
        getSearchBar().setActionText("New " + EntityUtil.getName(Order.class));
        // getBinder().addValueChangeListener(e -> getPresenter().onValueChange(isDirty()));
    }


    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

//    @Override
//    protected CrudEntityPresenter<Order> getPresenter() {
//        return presenter;
//    }

    @Override
    protected OrderPresenter getPresenter() {
        return presenter;
    }


    @Override
    protected String getBasePage() { return MarketConst.PAGE_STOREFRONT; }

    @Override
    public BeanValidationBinder<Order> getBinder() {
        return null;
    }

    @Override
    public SearchBar getSearchBar() {
        return searchBar;
    }

    @Override
    public Grid<Order> getGrid() {
        return grid;
    }

    @Override
    public OrderEditor getForm() {
        return (OrderEditor) super.getForm();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long orderId) {
        boolean editView = event.getLocation().getPath().contains(MarketConst.PAGE_STOREFRONT_EDIT);
        if (orderId != null) {
//            presenter.onNavigation(orderId, editView);
            presenter.loadEntity(orderId);
        } else if (getDialog().isOpened()) {
            presenter.closeSilently();
        }
    }
    public void navigateToMainView() {
        getUI().ifPresent(ui -> ui.navigate(MarketConst.PAGE_CUSTOMERS));
    }


    @Override
    public boolean isDirty() {
        return getForm().hasChanges();
    }

    @Override
    public void write(Order entity) throws ValidationException {
        getForm().write(entity);

    }

    public void setDialogElementsVisibility(boolean editing) {
        //dialog.add(editing ? orderEditor : orderDetails);
        getForm().setVisible(editing);
       // orderDetails.setVisible(!editing);
    }

    public void navigateToEntity(String id) {
        getUI().ifPresent(ui-> ui.navigate(TemplateUtils.generateLocation(getBasePage(), id)));

    }

    public OrderEditor getOpenedOrderEditor() {
        return getForm();
    }

    public void setOpened(boolean isOpened) {
        getDialog().setOpened(isOpened);
    }

    @Override
    public void clear() {
        getForm().clear();
    }
}
