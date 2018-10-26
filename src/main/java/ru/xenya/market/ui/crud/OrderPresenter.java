package ru.xenya.market.ui.crud;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.service.OrderService;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;
import ru.xenya.market.ui.utils.converters.OrderStateConverter;
import ru.xenya.market.ui.views.orderedit.OrderEditor;
import ru.xenya.market.ui.views.orderedit.OrdersViewOfCustomer;

import java.time.LocalDate;
import java.util.List;


@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderPresenter extends CrudEntityPresenter<Order> {

    private OrdersViewOfCustomer view;

    private final EntityPresenter<Order, OrdersViewOfCustomer> entityPresenter;
//    private final OrdersGridDataProvider dataProvider;
    private final User currentUser;
    private final OrderService orderService;
    private Customer currentCustomer;
    private Order currentOrder;

    public OrderPresenter(EntityPresenter<Order, OrdersViewOfCustomer> entityPresenter,
                          OrderService orderService, User currentUser) {
        super(orderService, currentUser);
        this.entityPresenter = entityPresenter;
//        this.dataProvider = dataProvider;
        this.orderService = orderService;
        //this.currentCustomer = currentCustomer;
        this.currentUser = currentUser;

    }

    public void setView(OrdersViewOfCustomer view) {
        this.view = view;
        //view.getSearchBar().addActionClickListener(e-> createNew());
        view.getGrid().setItems(updateList());
    }


    public void init(OrdersViewOfCustomer view) {
        this.view = view;
        this.entityPresenter.setView(view);
        this.view.getGrid().setItems(updateList());
        this.view.getOpenedOrderEditor().setCurrentUser(currentUser);
        this.view.getOpenedOrderEditor().addCancelListener(e -> cancel());
        this.view.getOpenedOrderEditor().addSaveListener(e -> save());
       // this.view.getOpenedOrderEditor().addCommentListener(e -> addComment(e.getMessage()));
        //todo добавить OrderDetails


//        view.getOpenedOrderDetails().addSaveListenter(e -> save());
//        view.getOpenedOrderDetails().addCancelListener(e -> cancel());
//        view.getOpenedOrderDetails().addBackListener(e -> back());
//        view.getOpenedOrderDetails().addEditListener(e -> edit());
//        view.getOpenedOrderDetails().addCommentListener(e -> addComment(e.getMessage()));
    }

    public void onNavigation(Long id, boolean edit) {

        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    public void createNewOrder(){
        open(entityPresenter.createNew(), true);
    }

    @Override
    public void cancel() {
        //todo проверку на несохраненные данные
        entityPresenter.cancel(() -> close(), () -> view.setOpened(true));
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

    @Override
    public Order createNew() {
       // orderService.setCurrentCustomer(currentCustomer);
        System.out.println("from orderpresenter->createNew->currentCustomer: " + orderService.getCurrentCustomer().getFullName());
//        Order order = orderService.createNew(currentUser);
        Order order = entityPresenter.createNew();
        System.err.println("from orderPresenter->create order " + order);
//        open(entityPresenter.createNew(), true);
        open(order, true);
        return /*orderService.createNew(currentUser)*/  order;
    }

//    public void load(Order order){
//        System.err.println("from load of OrderPresenter: # order " + order.getId());
//        System.err.println("from load of OrderPresenter: " + loadEntity(order.getId(), this::open));
//    }
    public Order open(Order entity){
        System.err.println("from OrderPresenter-> open()" + entity);
       // view.getBinder().readBean(entity);
        view.getForm().read(entity, false);
        view.updateTitle(false);
        view.openDialog();
//        view.getDialog().add(view.getForm());
//        view.getDialog().open();

        return entity;
    }

    public void open(Order order, boolean edit) {
        view.setDialogElementsVisibility(edit);
        view.setOpened(true);
        if (edit) {
            view.getOpenedOrderEditor().read(order, entityPresenter.isNew());
          //  view.getDialog().add(view.getForm());
         //   view.getDialog().open();

       }
    }

    public EntityPresenter<Order, OrdersViewOfCustomer> getEntityPresenter() {
        return entityPresenter;
    }

    public void save() {
        currentOrder = entityPresenter.getEntity();
        //Order order = view.getForm().getBinder().getBean();

        System.err.println("from save currentOrder = entityPresenter.getEntity() " + entityPresenter.getEntity());
        try {
            view.write(currentOrder);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        entityPresenter.save(e->{
                if (entityPresenter.isNew()){
                    view.showCreatedNotification();
                    view.getGrid().setItems(updateList());
                } else {
                    view.showUpdateNotification();
                    view.getGrid().setItems(updateList());
                }

                close();
            });

//       orderService.saveOrder(order);
//        if (writeEntity()){
//           super.save(e->{
//                if (isNew()) {
//                    getView().showCreatedNotification();
////                    updateList();
//                    view.getGrid().setItems(updateList());
//                } else {
//                    getView().showUpdateNotification();
//                    view.getGrid().setItems(updateList());
//                }
//                closeSilently();
//            });
//        }
    }



    public void delete(){
        super.delete(e->{
            getView().showDeleteNotification();
            view.getGrid().setItems(updateList());
            closeSilently();
        });
    }

    public void close() {
        view.setOpened(false);
        view.getOpenedOrderEditor().close();
        view.navigateToMainView();
        entityPresenter.close();
    }

    public void closeSilently() {
       // close();
       //getView().closeDialog();
//        close();
        entityPresenter.close();
        view.getDialog().close();
    }
    //}

    void addComment(String comment){
        if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser, e, comment))) {
            open(entityPresenter.getEntity(), false);
        }
    }


}

