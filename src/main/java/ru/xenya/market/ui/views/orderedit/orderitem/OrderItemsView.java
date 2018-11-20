package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.*;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.utils.converters.UnitConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Designer generated component for the order-items-view.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("order-items-view")
@HtmlImport("src/views/orderedit/order-items/order-items-view.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderItemsView extends PolymerTemplate<OrderItemsView.OrderItemsViewModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<OrderItemsView, List<OrderItem>>, List<OrderItem>> {

    @Id("grid")
    private Grid<OrderItem> grid;

    @Id("dialog")
    private Dialog dialog;
    @Id("add")
    private Button add;

    private Order order;
    private final User currentUser;

    private OrderItemsEditor editor;

    private boolean hasChanges = false;

    private final AbstractFieldSupport<OrderItemsView, List<OrderItem>> fieldSupport;

    private List<OrderItem> orderItemList;

    private Price currentPrice;




    /**
     * Creates a new OrderItemsView.
     */
    public OrderItemsView(OrderItemsEditor editor, User user) {
        currentUser = user;

        this.fieldSupport = new AbstractFieldSupport<>(this, new ArrayList<>(),
                Objects::equals, c->{});

        this.editor = editor;
        editor.setOrderItemsView(this);
        setupGrid();


//
//        Iterator<OrderItem> iterator = grid.getSelectedItems().iterator();
//        if (iterator.hasNext()) {
//            currentItem = iterator.next();
//        }
//
        add.addClickListener(event -> createNew());

        editor.addSaveListener(e -> {

            OrderItem value = editor.getValue();
            setValue(Stream.concat(getValue().stream(), Stream.of(editor.getValue())).collect(Collectors.toList()));
          //  update();
            dialog.setOpened(false);
        });


    }

    private void update() {
        grid.setItems(fieldSupport.getValue());
    }

    private void createNew() {
        editor.setValue(new OrderItem(currentUser));
        editor.setCurrentPrice(currentPrice);
        dialog.add(editor);
        dialog.open();
    }

    private void setupGrid() {
        UnitConverter unitConverter = new UnitConverter();
        grid.addColumn(OrderItem::getId).setHeader("#").setWidth("70px").setFlexGrow(0);
        grid.addColumn(OrderItem::getService).setWidth("150px").setHeader("Услуга").setFlexGrow(5);
        grid.addColumn(new ComponentRenderer<>(Div::new, (div, orderitem)->div.setText(
                Double.toString(orderitem.getQuantity()) + " " + unitConverter.encode(orderitem.getUnit()))
        )).setWidth("100px").setHeader("Кол-во");
        grid.addColumn(orderItem -> Integer.toString(orderItem.getDates().size())).setHeader("Выходы").setWidth("50px");
        grid.addColumn(OrderItem::getTotalPrice).setHeader("Сумма").setWidth("70px");
      //  grid.setItems(order.getItems());
    }

    @Override
    public void setValue(List<OrderItem> items) {
        fieldSupport.setValue(items);
        orderItemList = items;
        hasChanges = false;

        if (items != null) {
            grid.setItems(items);
            grid.addSelectionListener(
                    e->{
                        e.getFirstSelectedItem().ifPresent(entity->{
                            editor.setValue(entity);
                            dialog.setOpened(true);
                            grid.deselectAll();
                        });
                    });
        }
    }


    @Override
    public List<OrderItem> getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<OrderItemsView, List<OrderItem>>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    public Grid<OrderItem> getGrid() {
        return grid;
    }

    public void setDefaultPrice(Price defaultPrice) {
        this.currentPrice = defaultPrice;
        editor.setCurrentPrice(defaultPrice);
    }


    /**
     * This model binds properties between OrderItemsView and order-items-view.html
     */
    public interface OrderItemsViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
