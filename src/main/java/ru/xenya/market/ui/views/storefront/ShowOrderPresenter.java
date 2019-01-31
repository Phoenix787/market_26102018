package ru.xenya.market.ui.views.storefront;

import com.vaadin.flow.component.HasValue;
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
import ru.xenya.market.ui.dataproviders.OrdersGridDataProvider;
import ru.xenya.market.ui.dataproviders.ShowCaseDataProvider;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.utils.messages.CrudErrorMessage;
import ru.xenya.market.ui.views.orderedit.OrdersViewOfCustomer;
import ru.xenya.market.ui.views.storefront.beans.OrderCardHeader;

import java.util.List;
import java.util.stream.Collectors;


@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowOrderPresenter/* extends CrudEntityPresenter<Order>*/ {

    private ShowCase view;
    private OrderCardHeaderGenerator headersGenerator;

    private final EntityPresenter<Order, ShowCase> entityPresenter;
    private final ShowCaseDataProvider dataProvider;
    private final User currentUser;
    private final OrderService orderService;
    private final PriceService priceService;
    private Customer currentCustomer;
    private Order currentOrder;
    private Price currentPrice;


    @Autowired
    public ShowOrderPresenter(EntityPresenter<Order, ShowCase> entityPresenter,
                              ShowCaseDataProvider dataProvider,
                              OrderService orderService,
                              User currentUser, PriceService priceService) {
        this.dataProvider = dataProvider;
        this.orderService = orderService;
        this.entityPresenter = entityPresenter;
        this.currentUser = currentUser;
        this.priceService = priceService;

        //если делать витрину заказов
        headersGenerator = new OrderCardHeaderGenerator();
        headersGenerator.resetHeaderChain(false);
        dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
    }

    void init(ShowCase view) {
        this.entityPresenter.setView(view);
        this.view = view;
        view.getGrid().setDataProvider(dataProvider);
        view.getOpenedOrderEditor().setCurrentUser(currentUser);
        view.getOpenedOrderEditor().addCancelListener(e -> cancel());
        view.getOpenedOrderEditor().addSaveListener(e -> save());
        view.getOpenedOrderEditor().addDeleteListener(e -> delete());
        setCurrentPrice(getDefaultPrice());
    }


    OrderCardHeader getHeaderByOrderId(Long id){
        return headersGenerator.get(id);
    }


    public void filterChanged(String filter, boolean showPrevious) {
        headersGenerator.resetHeaderChain(showPrevious);
        dataProvider.setFilter(new ShowCaseDataProvider.OrderFilter(filter, showPrevious));
    }

    void onNavigation(Long id, boolean edit) {
        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    void createNewOrder(){
        open(entityPresenter.createNew(), false);
    }

    public void cancel() {
        entityPresenter.cancel(this::close, () -> view.setOpened(true));
//        view.closeDialog();
    }

    public void closeSilently() {
        entityPresenter.close();
        view.getOpenedOrderEditor().close();
        //  view.closeDialog();
        view.getConfirmDialog().setOpened(false);
//        view.navigateToMainView();
    }

    void edit() {
        UI.getCurrent().navigate(MarketConst.PAGE_STOREFRONT + "/" + entityPresenter.getEntity().getId());
    }

    void back() {
        view.setDialogElementsVisibility(true);
    }

//    public List<Order> updateList() {
//        return orderService.findByCustomer(currentCustomer);
//    }

    public ShowCaseDataProvider getDataProvider() {
        return dataProvider;
    }

//    public void filter(String filter) {
//              view.getGrid().setItems(updateList(filter));
//    }

    public List<Order> updateList(String filter) {
        if (filter != null ) {
            return orderService.findOrdersByStateOrPayment(currentCustomer, filter);
        } else {
            return orderService.findByCustomer(currentCustomer);
        }

    }


    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
        orderService.setCurrentPrice(currentPrice);
        view.getOpenedOrderEditor().setDefaultPrice(currentPrice);
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
        view.getOpenedOrderEditor().read(entity, false);
        view.setOpened(true);
        view.updateTitle(false);
//        view.openDialog();

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

    public EntityPresenter<Order, ShowCase> getEntityPresenter()
    {
        return entityPresenter;
    }

    public void save() {

        currentOrder = entityPresenter.getEntity();
       // currentOrder.addHistoryItem(currentUser, "Заказ изменён");
        List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
        if (fields.isEmpty()){
            if(writeEntity(currentOrder)){
                entityPresenter.save(e->{
                    if (entityPresenter.isNew()){
                        view.showCreatedNotification("Заказ");
//                        dataProvider.refreshAll();
                        view.getUI().ifPresent(ui->ui.access(dataProvider::refreshAll));
                    } else {
                        view.showUpdateNotification("Заказ # " + e.getId());
//                        dataProvider.refreshItem(e);
                        view.getUI().ifPresent(ui->ui.access(()->dataProvider.refreshItem(e)));
                    }
                    close();
                });
            }

        }

    }


    private boolean writeEntity(Order order) {
        try {
            view.write(order);
            return true;
        } catch (ValidationException e) {
            view.showError(CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void delete(){
        entityPresenter.delete(e->{
            view.showDeleteNotification("Заказ # " + e.getId());
//            view.getGrid().setItems(updateList());
            dataProvider.refreshAll();
            closeSilently();
        });
    }

    public void close() {
        view.getOpenedOrderEditor().close();
        view.setOpened(false);
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

