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
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.ui.utils.converters.UnitConverter;

import java.util.*;

/**
 * A Designer generated component for the order-items-view.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("order-items-view")
@HtmlImport("src/views/orderedit/order-items/order-items-view.html")
public class OrderItemsView extends PolymerTemplate<OrderItemsView.OrderItemsViewModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<OrderItemsView, List<OrderItem>>, List<OrderItem>> {

    @Id("grid")
    private Grid<OrderItem> grid;

    @Id("dialog")
    private Dialog dialog;
    @Id("add")
    private Button add;

    private Order order;

    private OrderItemsEditor editor;

    private boolean hasChanges = false;

    private final AbstractFieldSupport<OrderItemsView, List<OrderItem>> fieldSupport;

    private OrderItem currentItem;




    /**
     * Creates a new OrderItemsView.
     */
    public OrderItemsView() {

        this.fieldSupport = new AbstractFieldSupport<>(this, Collections.emptyList(),
                Objects::equals, c->{});

        editor = new OrderItemsEditor();
        setupGrid();


//
//        Iterator<OrderItem> iterator = grid.getSelectedItems().iterator();
//        if (iterator.hasNext()) {
//            currentItem = iterator.next();
//        }
//
        add.addClickListener(event -> createNew());

    }

    private void createNew() {
        editor.setValue(null);
        dialog.add(editor);
        dialog.open();
    }

    private void setupGrid() {
        UnitConverter unitConverter = new UnitConverter();
        grid.addColumn(OrderItem::getId).setHeader("#").setWidth("70px").setFlexGrow(0);
        grid.addColumn(OrderItem::getService).setWidth("150px").setHeader("Услуга").setFlexGrow(5);
        grid.addColumn(new ComponentRenderer<>(Div::new, (div, orderitem)->div.setText(
                Integer.toString(orderitem.getQuantity()) + " " + unitConverter.encode(orderitem.getUnit()))
        )).setWidth("100px").setHeader("Кол-во");
        grid.addColumn(orderItem -> Integer.toString(orderItem.getDates().size())).setHeader("Выходы").setWidth("50px");
        grid.addColumn(OrderItem::getTotalPrice).setHeader("Сумма").setWidth("70px");
      //  grid.setItems(order.getItems());
    }

    @Override
    public void setValue(List<OrderItem> items) {
        fieldSupport.setValue(items);
        hasChanges = false;

        if (items != null) {
            grid.setItems(items);
            grid.addSelectionListener(
                    e->{
                        e.getFirstSelectedItem().ifPresent(entity->{
                            editor.setValue(entity);
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


    /**
     * This model binds properties between OrderItemsView and order-items-view.html
     */
    public interface OrderItemsViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
