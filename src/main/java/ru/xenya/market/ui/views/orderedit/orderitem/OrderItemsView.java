package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.dataproviders.PriceDataProvider;
import ru.xenya.market.ui.utils.converters.UnitConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Designer generated component for the order-items-view.html template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("order-items-view")
@HtmlImport("src/views/orderedit/order-items/order-items-view.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderItemsView extends PolymerTemplate<OrderItemsView.OrderItemsViewModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<OrderItemsView, List<OrderItem>>, List<OrderItem>> {

    private final User currentUser;
    private final AbstractFieldSupport<OrderItemsView, List<OrderItem>> fieldSupport;
    @Id("grid")
    private Grid<OrderItem> grid;
    @Id("dialog")
    private Dialog dialog;
    @Id("add")
    private Button add;
    private OrderItemsEditor editor;
    private boolean hasChanges = false;
    private List<OrderItem> orderItemList;

    private Price currentPrice;
    private OrderItem oldOrderItem;


    /**
     * Creates a new OrderItemsView.
     */
    public OrderItemsView(/*OrderItemsEditor editor,*/PriceDataProvider priceDataProvider, User user) {
        currentUser = user;
        this.editor = new OrderItemsEditor();
        setDefaultPrice(priceDataProvider.getDefaultPrice());

        this.fieldSupport = new AbstractFieldSupport<>(this, new ArrayList<>(),
                Objects::equals, c -> {
        });

        editor.setOrderItemsView(this);
        setupGrid();
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);


        add.addClickListener(event -> createNew());

        editor.addSaveListener(e -> {
            if (oldOrderItem == null) {
                save(editor.getValue(), true);
            } else {
                save(editor.getValue(), false);
            }
        });

        editor.addDeleteListener(e -> {
            OrderItemsEditor source = (OrderItemsEditor) e.getSource();
            OrderItem orderItem = source.getValue();
            setValue(getValue().stream().filter(element -> element != orderItem).collect(Collectors.toList()));
            dialog.setOpened(false);
        });

        editor.addCancelListener(e -> {
            clear();
            dialog.setOpened(false);
        });
    }

    private void createNew() {
        editor.read(new OrderItem(currentUser), true);
        editor.setAddButtonText("Добавить");
        editor.setCurrentPrice(currentPrice);
        dialog.add(editor);
        dialog.open();
    }

    private void setupGrid() {
        UnitConverter unitConverter = new UnitConverter();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(OrderItem::getId).setHeader("#").setWidth("70px").setFlexGrow(0);
        grid.addColumn(OrderItem::getService).setWidth("150px").setHeader("Услуга").setFlexGrow(5);
        grid.addColumn(new ComponentRenderer<>(Div::new, (div, orderitem) -> div.setText(
                Double.toString(orderitem.getQuantity()) + " " + unitConverter.encode(orderitem.getUnit()))
        )).setWidth("100px").setHeader("Кол-во");
        grid.addColumn(orderItem -> Integer.toString(orderItem.getDates().size())).setHeader("Выходы").setWidth("50px");
        grid.addColumn(OrderItem::getTotalPrice).setHeader("Сумма").setWidth("70px");
        grid.addSelectionListener(
                e -> {
                    e.getFirstSelectedItem().ifPresent(entity -> {
                        oldOrderItem = new OrderItem(entity);
                        editor.setAddButtonText("Сохранить");
                        editor.read(entity, false);
                        dialog.add(editor);
                        dialog.setOpened(true);
                        grid.deselectAll();
                    });
                });
    }

    @Override
    public List<OrderItem> getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(List<OrderItem> items) {
        fieldSupport.setValue(items);
        orderItemList = items;
        hasChanges = false;

        if (items != null) {
            grid.setItems(items);
//            grid.addSelectionListener(
//                    e -> {
//                        e.getFirstSelectedItem().ifPresent(entity -> {
//                            oldOrderItem = new OrderItem(entity);
//                            editor.setAddButtonText("Сохранить");
//                            editor.read(entity, false);
//                            dialog.add(editor);
//                            dialog.setOpened(true);
//                            grid.deselectAll();
//                        });
//                    });
        }
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

    private void save(OrderItem entity, boolean isNew) {
      //  List<OrderItem> items = getValue().stream().filter(element -> element != oldOrderItem).collect(Collectors.toList());
        if (isNew) {
            setValue(Stream.concat(getValue().stream(), Stream.of(entity)).collect(Collectors.toList()));
        } else {
            setValue(getValue());
            oldOrderItem = null;
        }
        dialog.setOpened(false);
    }


    /**
     * This model binds properties between OrderItemsView and order-items-view.html
     */
    public interface OrderItemsViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
