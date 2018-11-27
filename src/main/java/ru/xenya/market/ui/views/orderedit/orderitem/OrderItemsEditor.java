package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.ui.components.PriceItemField;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.DeleteEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A Designer generated component for the order-items-editor.html template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("order-items-editor")
@HtmlImport("src/views/orderedit/order-items/order-items-editor.html")
//@SpringComponent
//@UIScope
public class OrderItemsEditor extends PolymerTemplate<OrderItemsEditor.OrderItemsEditorModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>, OrderItem> {


//    @Id("grid")
//    private Grid<ScheduleDates> grid;
//
//    @Id("dates")
//    private SelectedDates dates;
//
//    @Id("amount")
//    private AmountField amount;
//
//    @Id("totalPrice")
//    private Div totalPriceDiv;

//    private final OrderItemPresenter presenter;
//    private final ScheduleDatesService datesService;

//    @Id("selectedDates")
//    private Div selectedDates;
//
//    @Id("selected")
//    private Span selected;
//
//    @Id("discount")
//    private RadioButtonGroup<Discount> discountGroup;
//
//    @Id("dimensionsContainer")
//    private Div dimensionsContainer;
    //private final PriceService priceService;

    private final AbstractFieldSupport<OrderItemsEditor, OrderItem> fieldSupport;
    @Id("metaContainer")
    private Div metaContainer;
    @Id("ItemId")
    private Span itemId;
    @Id("title")
    private H2 title;
    @Id("service")
    private ComboBox<Service> serviceCb;
    @Id("unit")
    private ComboBox<Unit> unitCb;
    @Id("price")
    private ComboBox<PriceItem> priceCb;
    @Id("delete")
    private Button delete;
    @Id("add")
    private Button add;
    @Id("cancel")
    private Button cancel;

    private boolean isItemNew;
    private Price defaultPrice;
    private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);
    private OrderItem currentOrderItem;
    private PriceItem currentPriceItem;

    private OrderItemsView orderItemsView;

    private int totalPrice;
    private TextField widthField = new TextField("Ширина");
    private TextField heightField = new TextField("Высота");
    private Label label = new Label("X");



    /**
     * Creates a new OrderItemsEditor.
     */
    public OrderItemsEditor(/*OrderItemPresenter presenter, ScheduleDateProvider provider, ScheduleDatesService datesService, PriceService priceService*/) {
        // You can initialise any data required for the connected UI components here.

//        this.presenter = presenter;
//        this.datesService = datesService;
//        this.priceService = priceService;
        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c -> {
        });

        heightField.setEnabled(false);
        setupDiscountGroup();
        //  setupGrid(provider);
        setAddButtonText("Добавить");

        serviceCb.setItems(Service.values());
        unitCb.setItems(Unit.values());
        unitCb.addValueChangeListener(e -> {
            List<PriceItem> priceItemList = getPriceItems(e.getValue(), serviceCb.getValue());
            priceCb.setItems(priceItemList);
        });

        binder.bind(priceCb, "price");

//        widthField.addValueChangeListener(e-> heightField.setEnabled(true));
//        heightField.addValueChangeListener(e->
//                        amount.setValue(countAmount(Double.parseDouble(widthField.getValue()),
//                                Double.parseDouble(heightField.getValue()))));
//
//        price.setRenderer(TemplateRenderer.<PriceItem> of(
//                "<div>[[item.price]]<br><small>[[item.name]]</small></div>")
//                .withProperty("price", PriceItem::getPrice)
//                .withProperty("name", PriceItem::getName));
//        price.setItemLabelGenerator(PriceItem::getName);
//        price.setItems(getPriceItems(unit.getValue()));
//        price.addValueChangeListener(e->{
//            setPrice();
//
//        });


//
//        binder.bind(discountGroup, "discount");
//
//       // amount.addValueChangeListener(e -> setPrice());
//        binder.bind(amount, "quantity");
//
//        binder.bind(dates, "dates");

//
//        ReadOnlyHasValue<List<ScheduleDates>> datesReadOnlyHasValue = new ReadOnlyHasValue<>(
//                scheduleDates -> selectedDates.setText(scheduleDates.stream()
//                        .map(ScheduleDates::getDate).map(e->e.format(DateTimeFormatter.ISO_LOCAL_DATE))
//                        .collect(Collectors.joining(", "))));
//        binder.forField(datesReadOnlyHasValue).bind(OrderItem::getDates, null);
        // priceField.addPriceChangeListener(e -> priceChanged(priceField, e.getPriceItem()));
        //  priceField.addServiceChangeListener(e -> serviceChanged(priceField, e.getService()));
//        priceField.addUnitChangeListener(e -> unitChanged(priceField, e.getUnit()));
//        binder.bind(priceField, "price");

        add.addClickListener(e -> {
            if (currentPriceItem != null) {
                this.currentOrderItem.setPrice(currentPriceItem);
            }
            setValue(this.currentOrderItem);
            fireEvent(new SaveEvent(this, false));
        });
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, false)));

        cancel.addClickListener(e -> {
            close();
            fireEvent(new CancelEvent(this, false));
        });

        setPrice();

    }

    private List<PriceItem> getPriceItems(Unit unit, Service service) {
        return defaultPrice.getItemsPrice().stream()
                .filter(item -> item.getUnit().equals(unit))
                .filter(item -> item.getService().equals(service))
                .collect(Collectors.toList());
    }


    private void priceChanged(PriceItemField source, PriceItem priceItem) {
//        if(currentPriceItem != priceItem && priceItem == null) {
//            currentPriceItem = priceItem;
//         //   source.setCurrentPriceItem(priceItem);
//            // source.setValue(priceItem);
//            currentOrderItem.setPrice(priceItem);
//            setValue(currentOrderItem);
//        }

    }

    private void serviceChanged(PriceItemField source, Service service) {
        //  if (service != null) {
//            if (currentOrderItem == null) {
//                currentOrderItem = new OrderItem(currentUser);
//                setValue(currentOrderItem);
//                binder.setBean(currentOrderItem);
//            }
        if (currentPriceItem == null) {
            currentPriceItem = new PriceItem();
        }

        currentPriceItem.setService(service);
//            source.setCurrentPriceItem(currentPriceItem);


        // }
    }

    private void unitChanged(PriceItemField source, Unit unit) {
//        currentPriceItem = source.getCurrentPriceItem();
////        currentPriceItem.setUnit(unit);
////        source.setCurrentPriceItem(currentPriceItem);
////        priceField.hasChange(true);
        //   currentOrderItem = binder.getBean();
//        currentOrderItem.getPrice().setUnit(unit);
//        source.setValue(currentOrderItem.getPrice());
//        binder.setBean(currentOrderItem);
    }

    private Double countAmount(double width, double height) {
        return width * height;
    }
//
//    private List<PriceItem> getPriceItems(Unit unit) {
//        List<PriceItem> prices;
//        if (pricePlan.getValue() != null){
//            prices = pricePlan.getValue().getItems()
//                    .stream().filter(e-> e.getUnit().equals(unit)).collect(Collectors.toList());
//        } else {
//            prices = defaultPrice.getItems();
//        }
//        return prices;
//    }

    private void setPrice() {
//        int oldPrice = totalPrice;
//        Double selectedAmount = amount.getValue();
//        PriceItem priceItem = price.getValue();
//        totalPrice = 0;
//        if (selectedAmount != null && priceItem != null) {
//            totalPrice = (int) (selectedAmount * priceItem.getPrice());
//        }
//        totalPriceDiv.setText(FormattingUtils.formatAsCurrency(totalPrice));
//        if (oldPrice != totalPrice){
//            fireEvent(new PriceChangeEvent(this, oldPrice, totalPrice));
//        }
    }

    // private void setupGrid(ScheduleDateProvider provider) {
//        grid.addColumn(new LocalDateRenderer<>(ScheduleDates::getDate,
//                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Дата").setWidth("100px").setFlexGrow(5);
//        grid.addColumn(ScheduleDates::getWeekDay).setHeader("День недели").setWidth("100px").setFlexGrow(5);
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);
////        grid.asMultiSelect()
////                .addSelectionListener(event -> {
////                    Set<ScheduleDates> selected = event.getAllSelectedItems();
////                    selectedDates.setText(String.format(
////                            "%s, Выбрано дат: %d",
////                            event.getValue(),
////                            selected.size()));
////                });
//
//
////        grid.setDataProvider(provider);
//        grid.setItems(presenter.getDatesAfterNow());
//
//        GridMultiSelectionModel<ScheduleDates> selectionModel = (GridMultiSelectionModel<ScheduleDates>) grid.getSelectionModel();
//
////todo подумать над тем как это поле соединить с Set<ScheduleDates> selected = event.getValue();
//
//
////        grid.setItems(presenter.getDatesAfterNow());
//        grid.asMultiSelect().addValueChangeListener(event -> {
//            Set<ScheduleDates> selected = event.getValue();
//            List<LocalDate> collect = selected.stream().map(ScheduleDates::getDate).collect(Collectors.toList());
//            String result = collect.stream().map(e -> e.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect(Collectors.joining(", "));
//            this.selected.setText(String.format("Выбрано %d", selected.size()));
//            selectedDates.setText(String.format("%s", result));
//        });


    //   }

    private void setupDiscountGroup() {
//        discountGroup.setItems(Discount.none, Discount.ten, Discount.twenty, Discount.thirty);
//        //group.setRenderer(new TextRenderer<>(Person::getName));
//        discountGroup.setRenderer(new TextRenderer<>(Discount::toString));
    }

    private void setTotalPrice(int totalPrice) {
        getModel().setTotalPrice(FormattingUtils.formatAsCurrency(totalPrice));
    }

    @Override
    public OrderItem getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(OrderItem value) {
//        if (value.getPrice() == null) {
//            value.setPrice(defaultPrice.getItemsPrice().get(0));
//        }
        currentOrderItem = value;
        fieldSupport.setValue(value);
        binder.setBean(value);
        setPrice();
        //  createEditor(currentOrderItem);
    }


    public void clear() {
        binder.setBean(null);
    }

    public void close() {
        binder.setBean(null);
        //  priceField.setValue(null);
        setTotalPrice(0);
        // priceField.setValue(null);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    public void setCurrentPrice(Price defaultPrice) {
        this.defaultPrice = defaultPrice;
        priceCb.setItems(defaultPrice.getItemsPrice());
//        priceField.setPricePlan(defaultPrice);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public void setOrderItemsView(OrderItemsView orderItemsView) {
        this.orderItemsView = orderItemsView;
    }

    public String getAddButtonText() {
        return getModel().getButtonText();
    }

    public void setAddButtonText(String addButtonText) {
        getModel().setButtonText(addButtonText);
    }

//    public PriceItemField getPriceField() {
//        return priceField;
//    }

    public void read(OrderItem entity, boolean isNew) {
        this.isItemNew = isNew;
        this.itemId.setText(isNew ? "" : entity.getId().toString());
        this.metaContainer.setVisible(!isNew);
        this.title.setVisible(isNew);
        if (isNew){
            serviceCb.setItems(Service.values());
            unitCb.setItems(Unit.values());
            priceCb.setItems(defaultPrice.getItemsPrice());
        } else {
            serviceCb.setValue(entity.getService());
            unitCb.setValue(entity.getUnit());
            priceCb.setItems(getPriceItems(entity.getUnit(), entity.getService()));
        }


        setValue(entity);

    }

    /**
     * This model binds properties between OrderItemsEditor and order-items-editor.html
     */
    public interface OrderItemsEditorModel extends TemplateModel {
        // Add setters and getters for template properties here.
        void setTotalPrice(String totalPrice);

        String getButtonText();

        void setButtonText(String text);
    }

    private class PriceChangeEvent extends ComponentEvent<OrderItemsEditor> {
        private final int oldPrice;
        private final int totalPrice;

        public PriceChangeEvent(OrderItemsEditor editor, int oldPrice, int totalPrice) {
            super(editor, false);
            this.oldPrice = oldPrice;
            this.totalPrice = totalPrice;
        }
    }

    private class PricePlanChangeEvent extends ComponentEvent<OrderItemsEditor> {
        private final PriceItem value;

        public PricePlanChangeEvent(OrderItemsEditor editor, PriceItem value) {
            super(editor, false);
            this.value = value;
        }
    }


}
