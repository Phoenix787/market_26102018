package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.service.OrderService;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.ui.crud.EntityPresenter;
import ru.xenya.market.ui.utils.MarketConst;

import java.util.List;


@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderPresenter/* extends CrudEntityPresenter<Order>*/ {

    private OrdersViewOfCustomer view;

    private final EntityPresenter<Order, OrdersViewOfCustomer> entityPresenter;
//    private final OrdersGridDataProvider dataProvider;
    private final User currentUser;
    private final OrderService orderService;
    private final PriceService priceService;
    private Customer currentCustomer;
    private Order currentOrder;
    private Price currentPrice;

    @Autowired
    public OrderPresenter(EntityPresenter<Order, OrdersViewOfCustomer> entityPresenter,
                          OrderService orderService, User currentUser, PriceService priceService) {
       // super(orderService, currentUser);
        this.orderService = orderService;
        this.entityPresenter = entityPresenter;
//        this.dataProvider = dataProvider;
        //this.currentCustomer = currentCustomer;
        this.currentUser = currentUser;

        this.priceService = priceService;
    }

    public void setView(OrdersViewOfCustomer view) {
        this.view = view;
        //view.getSearchBar().addActionClickListener(e-> createNew());
        view.getGrid().setItems(updateList());
    }


    void init(OrdersViewOfCustomer view) {
        this.entityPresenter.setView(view);
        this.view = view;
        view.getGrid().setItems(updateList());
        view.getOpenedOrderEditor().setCurrentUser(currentUser);
        view.getOpenedOrderEditor().addCancelListener(e -> cancel());
        view.getOpenedOrderEditor().addSaveListener(e -> save());
        view.getOpenedOrderEditor().addDeleteListener(e -> delete());
        setCurrentPrice(getDefaultPrice());
        //todo добавить OrderDetails
    }

    void onNavigation(Long id, boolean edit) {

        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    void createNewOrder(){
        open(entityPresenter.createNew(), false);
    }

    public void cancel() {
        //todo проверку на несохраненные данные
        entityPresenter.cancel(this::close, () -> view.setOpened(true));
        //view.getDialog().close();
    }

    void edit() {
        UI.getCurrent().navigate(MarketConst.PAGE_STOREFRONT + "/" + entityPresenter.getEntity().getId());
    }

    void back() {
        view.setDialogElementsVisibility(true);
    }

    public List<Order> updateList() {
        return orderService.findByCustomer(currentCustomer);
    }


    public void filter(String filter) {
            if (filter != null && !filter.isEmpty()){
              view.getGrid().setItems(updateList(filter));
            } else {
                view.getGrid().setItems(updateList());
            }
    }

    public List<Order> updateList(String filter) {
        if (filter != null ) {
            return orderService.findOrdersByStateOrPayment(currentCustomer, filter);
        } else {
            return orderService.findByCustomer(currentCustomer);
        }

    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
        orderService.setCurrentCustomer(currentCustomer);
        view.getForm().setCurrentCustomer(currentCustomer);
    }

    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
        orderService.setCurrentPrice(currentPrice);
        view.getForm().setDefaultPrice(currentPrice);
    }

    public Order createNew() {
        Order order = entityPresenter.createNew();
        open(order, true);
        return order;
    }



    public void load(Long id) {

        entityPresenter.loadEntity(id, this::open);
    }

    public Order open(Order entity){
        view.getForm().read(entity, false);
        view.setOpened(true);
        view.updateTitle(false);
        view.openDialog();

        return entity;
    }

    public void open(Order order, boolean edit) {
        view.setDialogElementsVisibility(true);
        view.setOpened(true);
        if (edit) {
            view.getOpenedOrderEditor().read(order, entityPresenter.isNew());
       } else {
            view.updateTitle(true);
            view.getOpenedOrderEditor().read(order, entityPresenter.isNew());
        }
    }

    public EntityPresenter<Order, OrdersViewOfCustomer> getEntityPresenter()
    {
        return entityPresenter;
    }

    public void save() {

        currentOrder = entityPresenter.getEntity();

        try {
            view.write(currentOrder);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        entityPresenter.save(e->{
                if (entityPresenter.isNew()){
                    view.showCreatedNotification("Заказ");
                    view.getGrid().setItems(updateList());
                } else {
                    view.showUpdateNotification("Заказ # " + e.getId());
                    view.getGrid().setItems(updateList());
                }

                close();
            });
    }



    public void delete(){
        entityPresenter.delete(e->{
            view.showDeleteNotification("Заказ # " + e.getId());
            view.getGrid().setItems(updateList());
            closeSilently();
        });
    }

    public void close() {
        view.getOpenedOrderEditor().close();
        view.setOpened(false);
        view.navigateToMainView();
        entityPresenter.close();
    }

    public void closeSilently() {
       // view.getOpenedOrderEditor().close();
        view.closeDialog();
        view.getConfirmDialog().setOpened(false);
        view.navigateToMainView();
        entityPresenter.close();
    }

    void addComment(String comment){
        if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser, e, comment))) {
            open(entityPresenter.getEntity(), false);
        }
    }

    public Price getDefaultPrice(){
        return priceService.findPriceByDefault(true);
    }

}

