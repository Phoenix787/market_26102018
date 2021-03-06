package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.service.ScheduleDatesService;
import ru.xenya.market.ui.dataproviders.PriceDataProvider;
import ru.xenya.market.ui.dataproviders.ScheduleDateProvider;
import ru.xenya.market.ui.utils.FormattingUtils;
import ru.xenya.market.ui.utils.converters.UnitConverter;
import ru.xenya.market.ui.utils.messages.CrudErrorMessage;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

//    private final User currentUser;
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
    private int totalPrice = 0;


    /**
     * Creates a new OrderItemsView.
     */
    public OrderItemsView(/*OrderItemsEditor editor,*/PriceDataProvider priceDataProvider,
                                                      ScheduleDateProvider provider,
                                                      ScheduleDatesService datesService/*,
                                                      User user*/) {
//        currentUser = user;
        this.editor = new OrderItemsEditor(provider, datesService);
        setDefaultPrice(priceDataProvider.getDefaultPrice());

        this.fieldSupport = new AbstractFieldSupport<>(this, new ArrayList<>(),
                Objects::equals, c -> {
        });

        editor.setOrderItemsView(this);
        setupGrid();
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);

        dialog.setHeight("100%");
        dialog.getElement().addAttachListener(event -> UI.getCurrent().getPage().executeJavaScript(
                "$0.$.overlay.setAttribute('theme', 'center');", dialog.getElement()
        ));


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
            updateTotalPrice(getValue());
            dialog.setOpened(false);
        });

        editor.addCancelListener(e -> {
            setHasChanges(false);
            dialog.setOpened(false);
            editor.closeSilently();
        });

    }



    private void createNew() {
        editor.read(new OrderItem(/*currentUser*/), true);
        editor.setAddButtonText("Добавить");
      //  editor.setCurrentPrice(currentPrice);
        dialog.add(editor);
        dialog.open();
    }

    private void setupGrid() {
        UnitConverter unitConverter = new UnitConverter();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeightByRows(true);
        grid.addColumn(OrderItem::getId).setHeader("#").setResizable(true).setWidth("70px").setFlexGrow(0).setComparator((p1, p2)->p1.getId().compareTo(p2.getId()));
        grid.addColumn(OrderItem::getService).setResizable(true).setWidth("70px").setHeader("Услуга").setFlexGrow(1);
        grid.addColumn(new ComponentRenderer<>(Div::new, (div, orderitem) -> div.setText(
                FormattingUtils.formatAsDouble(orderitem.getQuantity()) + " " + unitConverter.encode(orderitem.getUnit()))
        )).setResizable(true).setWidth("70px").setHeader("Кол-во").setFlexGrow(2);
        grid.addColumn(orderItem -> Integer.toString(orderItem.getDates().size())).setHeader("Выходы").setWidth("30px");
//        grid.addColumn(orderItem -> FormattingUtils.formatListSize(orderItem.getDates())).setHeader("Выходы").setWidth("30px");
        grid.addColumn(orderItem->FormattingUtils.formatAsCurrency(orderItem.getTotalPrice())).setHeader("Сумма").setWidth("100px");
        grid.addSelectionListener(
                e -> {
                    e.getFirstSelectedItem().ifPresent(entity -> {
//                        if (oldOrderItem == null ) {
//                            oldOrderItem = new OrderItem(entity);
//                        } else
//                        if (!oldOrderItem.getId().equals(entity.getId())){
                            oldOrderItem = new OrderItem(entity);
                        //}
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
        totalPrice = 0;
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

    public Registration addPriceChangeListener(ComponentEventListener<TotalPriceEvent> listener) {
        return addListener(TotalPriceEvent.class, listener);
    }

    public Grid<OrderItem> getGrid() {
        return grid;
    }

    public void setDefaultPrice(Price defaultPrice) {
        this.currentPrice = defaultPrice;
        editor.setCurrentPrice(defaultPrice);
    }

    private void save(OrderItem entity, boolean isNew) {
        setHasChanges(true);
      //  List<HasValue<?, ?>> fields = editor.validate().collect(Collectors.toList());
    //    if (fields.isEmpty()){
            if (writeEntity(entity)){
                if (isNew) {
                    List<OrderItem> items = Stream.concat(getValue().stream(), Stream.of(entity)).collect(Collectors.toList());
                    setValue(items);
                    updateTotalPrice(items);
                } else {
                    List<OrderItem> items = getValue();
                    setValue(items);
                    updateTotalPrice(items);
                    oldOrderItem = null;
                }
                dialog.setOpened(false);
             //   editor.close();

            }
//        } else if (fields.get(0) instanceof Focusable) {
//            ((Focusable<?>) fields.get(0)).focus();
//        }


    }

    private void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
        if (hasChanges) {
            fireEvent(new ru.xenya.market.ui.views.orderedit.orderitem.ValueChangeEvent(this));
        }
    }

    public Stream<HasValue<?,?>> validate(){
        return getChildren()
                .filter(component -> fieldSupport.getValue().size() == 0)
                .map(editor -> ((OrderItemsEditor) editor).validate())
                .flatMap(hasValueStream -> hasValueStream);
    }

    /**
     * This model binds properties between OrderItemsView and order-items-view.html
     */
    public interface OrderItemsViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }

    public boolean writeEntity(OrderItem entity) {
        try {
            editor.write(entity);
            return true;
        } catch (ValidationException e) {
            Notification.show(CrudErrorMessage.REQUIRED_FIELDS_MISSING);
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void updateTotalPrice(List<OrderItem> items) {
        totalPrice = items.stream().map(OrderItem::getTotalPrice).reduce((x, y) -> x + y).orElse(0);
//        final int delta = newItemPrice - oldItemPrice;
//        totalPrice += delta;
        setHasChanges(true);
        fireEvent(new TotalPriceEvent(this, totalPrice));
    }


}
