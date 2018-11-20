package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.entity.*;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.backend.service.ScheduleDatesService;
import ru.xenya.market.ui.components.PriceItemField;
import ru.xenya.market.ui.dataproviders.ScheduleDateProvider;
import ru.xenya.market.ui.events.DeleteEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.util.Objects;

/**
 * A Designer generated component for the order-items-editor.html template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("order-items-editor")
@HtmlImport("src/views/orderedit/order-items/order-items-editor.html")
@SpringComponent
@UIScope
public class OrderItemsEditor extends PolymerTemplate<OrderItemsEditor.OrderItemsEditorModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>, OrderItem> {


//    @Id("pricePlan")
//    private ComboBox<Price> pricePlan;
//    @Id("service")
//    private ComboBox<Service> service;
//    @Id("unit")
//    private ComboBox<Unit> unit;
//    @Id("price")
//    private ComboBox<PriceItem> price;

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
    private final OrderItemPresenter presenter;
    private final ScheduleDatesService datesService;

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
    private final PriceService priceService;
    @Id("priceField")
    private PriceItemField priceField;
    @Id("delete")
    private Button delete;
    @Id("add")
    private Button add;
    private User currentUser;
    private Order currentOrder;
    private Price defaultPrice;

    private final AbstractFieldSupport<OrderItemsEditor, OrderItem> fieldSupport;
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
    public OrderItemsEditor(OrderItemPresenter presenter, ScheduleDateProvider provider, ScheduleDatesService datesService, PriceService priceService) {
        // You can initialise any data required for the connected UI components here.

        this.presenter = presenter;
        this.datesService = datesService;
        this.priceService = priceService;
        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c -> {
        });

        // defaultPrice = presenter.getDefaultPrice();
      //  priceField = new PriceItemField(priceService);

//        priceField.addServiceChangeListener(e -> serviceChanged(e.getSource(), e.getService()));
//        priceField.addPriceChangeListener(e -> {currentPriceItem = e.getPriceItem(); priceChanged(priceField, currentPriceItem);
//        });

        heightField.setEnabled(false);
        setupDiscountGroup();
        setupGrid(provider);


//        pricePlan.addValueChangeListener(e -> {
//            priceItemField.setPricePlan(e.getValue());
//            defaultPrice = e.getValue();
//            if (currentOrderItem == null) {
//                currentOrderItem = new OrderItem(currentUser);
//                currentOrderItem.setPricePlan(defaultPrice);
//                setValue(currentOrderItem);
//                binder.setBean(currentOrderItem);
//            }
//            fireEvent(new PricePlanChangeEvent(this, e.getValue()));
//        });

//        pricePlan.setDataProvider(DataProvider.fromCallbacks(
//                query -> {
//                    int offset = query.getOffset();
//                    int limit = query.getLimit();
//                    Stream<Price> stream = priceService.findAll().stream();
//                    return stream.skip(offset).limit(limit);
//                },
//                query -> (int) priceService.count())
//        );
//        pricePlan.setItemLabelGenerator(Price::toString);
//        pricePlan.setValue(defaultPrice);
//
//
//        binder.forField(pricePlan)
//                .withValidator(new BeanValidator(OrderItem.class, "pricePlan"))
//                .bind(OrderItem::getPricePlan, OrderItem::setPricePlan);


//        service.addValueChangeListener(e->{
//
//        });
//        service.setRenderer(TemplateRenderer.<Service> of("<div>[[item.service]]</div>")
//                .withProperty("service", Service::toString));
//        service.setDataProvider(DataProvider.ofItems(Service.values()));
//        binder.bind(service, "price.service");
//
//
//        unit.setItemLabelGenerator(Unit::toString);
//        unit.setDataProvider(DataProvider.ofItems(Unit.values()));
//        unit.addValueChangeListener(e->{
//            //если выбраны см2 нужно показать дополнительные дивы для ввода размеров
//            if (e.getValue().equals(Unit.cm2)){
//                dimensionsContainer.add(widthField, label, heightField);
//                amount.setValue(0.0);
//             //   amount.setEnabled(false);
//            }
//        });
//        binder.bind(unit, "price.unit");
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
        binder.bind(priceField, "price");

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

        setPrice();

        add.addClickListener(e -> {
            if (currentPriceItem != null){
                this.currentOrderItem.setPrice(currentPriceItem);
            }
            OrderItem currentOrderItem = this.currentOrderItem;
            setValue(this.currentOrderItem);
//          ArrayList<OrderItem> value = (ArrayList<OrderItem>) orderItemsView.getValue();
//
//          OrderItem item = fieldSupport.getValue();
//            value.add(item);
//
//            orderItemsView.setValue(Stream.concat(orderItemsView.getValue().stream(),
//                    Stream.of(currentOrderItem)).collect(Collectors.toList()));
//
////            //orderItemsView.setValue(value);
////            orderItemsView.getGrid().setItems(value);
            fireEvent(new SaveEvent(this, false));
        });
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, false)));

    }

    private void priceChanged(PriceItemField source, PriceItem priceItem) {
       // if (source.getCurrentPriceItem() == null) {
            currentPriceItem = priceItem;
            source.setCurrentPriceItem(priceItem);
            source.setValue(priceItem);
            currentOrderItem.setPrice(priceItem);
            setValue(currentOrderItem);
            fieldSupport.setValue(currentOrderItem);
    //    }

//        currentOrderItem.setPrice(priceItem);
//        source.setValue(priceItem);
//        setValue(currentOrderItem);
    }

    private void serviceChanged(PriceItemField source, Service service) {
      //  if (service != null) {
//            if (currentOrderItem == null) {
//                currentOrderItem = new OrderItem(currentUser);
//                setValue(currentOrderItem);
//                binder.setBean(currentOrderItem);
//            }
            priceField.hasChange(true);

       // }
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

    private void setupGrid(ScheduleDateProvider provider) {
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


    }

    private void setupDiscountGroup() {
//        discountGroup.setItems(Discount.none, Discount.ten, Discount.twenty, Discount.thirty);
//        //group.setRenderer(new TextRenderer<>(Person::getName));
//        discountGroup.setRenderer(new TextRenderer<>(Discount::toString));
    }

    private void setTotalPrice(int totalPrice) {
        getModel().setTotalPrice(FormattingUtils.formatAsCurrency(totalPrice));
    }

    public void close() {
        setTotalPrice(0);
    }

    @Override
    public OrderItem getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(OrderItem value) {
            currentOrderItem = value;
            fieldSupport.setValue(value);
            binder.setBean(value);
            setPrice();
            createEditor(currentOrderItem);
    }

    private void createEditor(OrderItem currentOrderItem) {
        priceField.setValue(currentOrderItem.getPrice() != null ? currentOrderItem.getPrice() : currentPriceItem);
        priceField.addPriceChangeListener(e -> priceChanged(priceField, e.getPriceItem()));
        priceField.setBinder(binder);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    public void setCurrentPrice(Price defaultPrice) {
        this.defaultPrice = defaultPrice;
        priceField.setPricePlan(defaultPrice);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public void setOrderItemsView(OrderItemsView orderItemsView) {
        this.orderItemsView = orderItemsView;
    }

    /**
     * This model binds properties between OrderItemsEditor and order-items-editor.html
     */
    public interface OrderItemsEditorModel extends TemplateModel {
        // Add setters and getters for template properties here.
        void setTotalPrice(String totalPrice);
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
