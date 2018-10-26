//package ru.xenya.market.ui.views.customers;
//
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.polymertemplate.Id;
//import com.vaadin.flow.data.renderer.ComponentRenderer;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import ru.xenya.market.backend.data.entity.Customer;
//import ru.xenya.market.backend.repositories.CustomerRepository;
//import ru.xenya.market.ui.MainView;
//import ru.xenya.market.ui.components.SearchBar;
//import ru.xenya.market.ui.components.common.AbstractEditorDialog;
//import ru.xenya.market.ui.utils.MarketConst;
//import ru.xenya.market.ui.views.orderedit.OrderView;
//import ru.xenya.market.ui.views.orderedit.OrdersViewOfCustomer;
//
//
//import java.util.List;
//
//import static ru.xenya.market.ui.utils.MarketConst.PAGE_CUSTOMERS;
//
////@Route(value = PAGE_CUSTOMERS, layout = MainView.class)
//@PageTitle(MarketConst.TITLE_CUSTOMERS)
//public class CustomerView extends VerticalLayout {
//
//    @Id("search")
//    private SearchBar search;
//
//    private CustomerRepository repository;
//
//    @Id("grid")
//    private Grid<Customer> grid;
//
//    @Id("newCustomer")
//    private Button addCustomer = new Button();
//
//
//    private final CustomerForm form;
//    private final OrderView orderView;
//
//
//    // private final BeanValidationBinder<Customer> binder = new BeanValidationBinder<>(Customer.class);
//
//    public CustomerView(CustomerRepository repository, OrderView orderView) {
//        this.repository = repository;
//        search = new SearchBar();
//
//        addCustomer.setText("Новый контрагент");
//        addCustomer.addClickListener(e -> openForm(new Customer(), AbstractEditorDialog.Operation.ADD));
//        this.form = new CustomerForm(this::saveUpdate, this::deleteUpdate);
//        this.orderView = orderView;
//      //  this.form.setBinder(binder);
//        setupSearchBar();
//        setupButtonBar();
//        setupGrid();
//        setupEventListeners();
//
//    }
//
//    private void setupSearchBar() {
//        search.getFilterTextField().addValueChangeListener(e->updateList(search.getFilter()));
//        search.setActionText("Новый контрагент");
//        search.addActionClickListener(e -> openForm(new Customer(), AbstractEditorDialog.Operation.ADD));
//        add(search);
//    }
//
//    private void setupButtonBar() {
//        HorizontalLayout buttonBar = new HorizontalLayout(addCustomer);
//        add(buttonBar);
//    }
//
//    private void openForm(Customer customer, AbstractEditorDialog.Operation operation) {
//        if (form.getElement().getParent() == null) {
//            getUI().ifPresent(ui->ui.add(form));
//        }
//        form.open(customer, operation);
//    }
//
//    public CustomerRepository getRepository() {
//        return repository;
//    }
//
//    private void saveUpdate(Customer customer, AbstractEditorDialog.Operation operation) {
//        repository.save(customer);
//        updateList(search.getFilter());
//        Notification.show("Контрагент успешно " + operation.getNameInText(), 3000, Notification.Position.BOTTOM_START);
//    }
//
//    private void deleteUpdate(Customer customer){
//        repository.delete(customer);
//        updateList(search.getFilter());
//        Notification.show("Контрагент успешно удален. ", 3000, Notification.Position.BOTTOM_START);
//    }
//
//    private void updateList(String filter) {
//      //  List<Customer> customers = repository.findByFullNameContainingIgnoreCase(filter);
//        List<Customer> customers = repository.findByFullNameContainingIgnoreCaseOrAddressContainingIgnoreCase(filter, filter);
//        grid.setItems(customers);
//    }
//
//    private void setupEventListeners() {
////        grid.addSelectionListener(e->{
////            e.getFirstSelectedItem().ifPresent(entity ->{
////              //  navigateToEntity(entity.getId().toString());
////                navigateToEntity(entity);
////                grid.deselectAll();
////            });
////        });
//
//    }
//
//    private void navigateToEntity(String id) {
//       // getUI().ifPresent(ui -> ui.navigate(TemplateUtils.generateLocation(getBasePage(), id)));
//
//    }
//    private void navigateToEntity(Customer customer) {
//        openForm(customer, AbstractEditorDialog.Operation.EDIT);
//    }
//
//    private void setupGrid() {
//        this.grid = new Grid<>();
//        grid.setHeight("100vh");
//        grid.addColumn(Customer::getId).setWidth("50px").setFlexGrow(0);
//        grid.addColumn(Customer::getFullName).setWidth("250px").setHeader("Наименование").setFlexGrow(5);
//        grid.addColumn(Customer::getAddress).setWidth("200px").setHeader("Адрес").setFlexGrow(5);
//        grid.addColumn(Customer::getPhoneNumbers).setWidth("250px").setFlexGrow(5);
//        grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setFlexGrow(2);
//        grid.addColumn(new ComponentRenderer<>(this::createOrderButton)).setFlexGrow(2);
//
//        updateList(search.getFilter());
//        add(grid);
//
//    }
//
//    private Button createEditButton(Customer customer) {
//        Button edit = new Button("Редактировать",
//                event -> form.open(customer, AbstractEditorDialog.Operation.EDIT));
//        edit.setIcon(new Icon("lumo", "edit"));
//        edit.addClassName("customer__edit");
//        edit.getElement().setAttribute("theme", "tertiary");
//        return edit;
//    }
//
//    private Button createOrderButton(Customer customer) {
//        Button edit = new Button("Заказы", event -> orderView.open(customer));
//        edit.setIcon(new Icon("vaadin", "cart"));
//        edit.addClassName("customer__edit");
//        edit.getElement().setAttribute("theme", "tertiary");
//        return edit;
//    }
//
//    public Grid<Customer> getGrid(){
//        return grid;
//    }
//
//    public String getBasePage(){
//        return PAGE_CUSTOMERS;
//    }
//
//
//}
