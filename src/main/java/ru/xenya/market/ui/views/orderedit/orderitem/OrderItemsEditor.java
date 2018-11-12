package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.Discount;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.data.entity.User;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

/**
 * A Designer generated component for the order-items-editor.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("order-items-editor")
@HtmlImport("src/views/orderedit/order-items/order-items-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderItemsEditor extends PolymerTemplate<OrderItemsEditor.OrderItemsEditorModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>, OrderItem> {


    @Id("pricePlan")
    private ComboBox<String> pricePlan;
    @Id("service")
    private ComboBox<String> service;
    @Id("unit")
    private ComboBox<String> unit;
    @Id("price")
    private ComboBox<String> price;
    @Id("grid")
    private Grid<ScheduleDates> grid;
    @Id("delete")
    private Button delete;
    @Id("add")
    private Button add;

    @Id("selectedDates")
    private Div selectedDates;
    @Id("discount")
    private RadioButtonGroup<Discount> discountGroup;

    private User currentUser;
    private Order currentOrder;
    private final AbstractFieldSupport<OrderItemsEditor, OrderItem> fieldSupport;
    private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);

    /**
     * Creates a new OrderItemsEditor.
     */
    public OrderItemsEditor() {
        // You can initialise any data required for the connected UI components here.

        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c -> { });
        setupDiscountGroup();
        setupGrid();
        //подключаем все поля к биндеру
    }

    private void setupGrid() {
        grid.addColumn(new LocalDateRenderer<>(ScheduleDates::getDate,
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Дата").setWidth("100px").setFlexGrow(5);
        grid.addColumn(ScheduleDates::getWeekDay).setHeader("День недели").setWidth("100px").setFlexGrow(5);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.asMultiSelect()
                .addSelectionListener(event -> {
                    selectedDates.setText(String.format(
                            "%s, Выбрано дат: %d",
                            event.getValue(),
                            event.getAllSelectedItems().size()));
                });
    }

    private void setupDiscountGroup() {
        discountGroup.setItems(Discount.none, Discount.ten, Discount.twenty, Discount.thirty);
        //group.setRenderer(new TextRenderer<>(Person::getName));
        discountGroup.setRenderer(new TextRenderer<>(Discount::toString));
    }

    public void open(OrderItem item) {
        //public void open(Customer customer) {
        //        customerName.setText(customer.getFullName());
        //        // presenter.init(this);
        //        presenter.setCurrentCustomer(customer);
        //        grid.setItems(presenter.updateList());
        //    }

       // grid.setItems(presenter.getDates());
    }

    @Override
    public void setValue(OrderItem value) {
        fieldSupport.setValue(value);
        binder.setBean(value);
    }

    @Override
    public OrderItem getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    /**
     * This model binds properties between OrderItemsEditor and order-items-editor.html
     */
    public interface OrderItemsEditorModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
