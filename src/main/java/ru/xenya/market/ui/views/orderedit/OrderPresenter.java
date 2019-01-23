package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.*;
import ru.xenya.market.backend.service.OrderService;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.ui.crud.EntityPresenter;
import ru.xenya.market.ui.dataproviders.OrdersGridDataProvider;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.utils.messages.CrudErrorMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderPresenter/* extends CrudEntityPresenter<Order>*/ {

    private OrdersViewOfCustomer view;

    private final EntityPresenter<Order, OrdersViewOfCustomer> entityPresenter;
    private final OrdersGridDataProvider dataProvider;
    private final User currentUser;
    private final OrderService orderService;
    private final PriceService priceService;
    private Customer currentCustomer;
    private Order currentOrder;
    private Price currentPrice;

    @Autowired
    public OrderPresenter(EntityPresenter<Order, OrdersViewOfCustomer> entityPresenter,
                          OrdersGridDataProvider dataProvider, OrderService orderService, User currentUser, PriceService priceService) {
        this.dataProvider = dataProvider;
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
//        view.getGrid().setDataProvider(dataProvider);
    }


    void init(OrdersViewOfCustomer view) {
        this.entityPresenter.setView(view);
        this.view = view;
//        view.getGrid().setItems(updateList());
        view.getGrid().setDataProvider(dataProvider);
        view.getOpenedOrderEditor().setCurrentUser(currentUser);
        view.getOpenedOrderEditor().addCancelListener(e -> cancel());
        view.getOpenedOrderEditor().addSaveListener(e -> save());
        view.getOpenedOrderEditor().addDeleteListener(e -> delete());
    //    view.getOpenedOrderEditor().addDeletePayEventListener(e->deletePayItem(currentOrder, e.getPayItem()));
        setCurrentPrice(getDefaultPrice());
        //todo добавить OrderDetails
    }

//    private void deletePayItem(Order order, Repayment payItem) {
//        if (order.getRepayments() != null){
//            order.getRepayments().remove(payItem);
//        }
//        entityPresenter.save(e->{
//            view.showNotification("Позиция платежа удалена, заказ обновлен");
//        });
//    }

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

    public OrdersGridDataProvider getDataProvider() {
        return dataProvider;
    }

    public void filter(String filter) {
              view.getGrid().setItems(updateList(filter));
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
        dataProvider.setFilter(new OrdersGridDataProvider.OrderFilter(currentCustomer.getFullName(), currentCustomer.getId(), false));
        view.getGrid().setDataProvider(dataProvider);



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
//        try {
//            view.getOpenedOrderEditor().setJasperPrint(exportToPDF(id));
//        } catch (IOException | JRException e) {
//            e.printStackTrace();
//        }

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
       // currentOrder.addHistoryItem(currentUser, "Заказ изменён");
        List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
        if (fields.isEmpty()){
            if(writeEntity(currentOrder)){
                entityPresenter.save(e->{
                    if (entityPresenter.isNew()){
                        view.showCreatedNotification("Заказ");
                      //  view.getGrid().setItems(updateList());
                       // dataProvider.refreshAll();
                        view.getUI().ifPresent(ui->ui.access(dataProvider::refreshAll));
                    } else {
                        view.showUpdateNotification("Заказ # " + e.getId());
                      //  view.getGrid().setItems(updateList());
                    //    dataProvider.refreshItem(e);
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

//    public JasperPrint exportToPDF(Long id) throws IOException, JRException {
//
//        final InputStream jreport = this.getClass().getResourceAsStream("/jasper/specification.jasper");
//        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(orderService.findById(id)));
//       Map<String, Object> parameters = new HashMap<>();
//        parameters.put("REPORT_LOCALE", MarketConst.APP_LOCALE );
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jreport, parameters, dataSource);
//        return jasperPrint;
//    }

    public JasperPrint generateSpecFor(Order order) throws IOException, JRException {
        final InputStream jReport = this.getClass().getResourceAsStream("/jasper/specification.jasper");
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("Specification"));
        Map<String, Object> parameters = parameters(order, MarketConst.APP_LOCALE);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jReport, parameters, dataSource);
        return jasperPrint;
    }

    private Map<String, Object> parameters(Order order, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("order", order);
        parameters.put("REPORT_LOCALE", locale);
        return parameters;
    }

    private InputStream export(Order order) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = generateSpecFor(order);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
           // JasperExportManager.exportReportToPdfFile(jasperPrint, "/spec.pdf");
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

//    private JasperReport loadTemplate() throws JRException {
//        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
//        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
//        return JasperCompileManager.compileReport(jasperDesign);
//    }

}

