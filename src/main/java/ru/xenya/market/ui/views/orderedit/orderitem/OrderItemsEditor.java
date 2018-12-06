package ru.xenya.market.ui.views.orderedit.orderitem;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.backend.data.Discount;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.service.ScheduleDatesService;
import ru.xenya.market.ui.components.SelectedDates;
import ru.xenya.market.ui.dataproviders.ScheduleDateProvider;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.DeleteEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.utils.FormattingUtils;
import ru.xenya.market.ui.utils.converters.AmountConverter;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;
import ru.xenya.market.ui.views.admin.prices.utils.PriceConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    @Id("grid")
    private Grid<ScheduleDates> grid;

    @Id("dates")
    private SelectedDates dates;


//
//    @Id("totalPrice")
//    private Div totalPriceDiv;

//    private final OrderItemPresenter presenter;
    private final ScheduleDatesService datesService;
    private final ScheduleDateProvider dateProvider;

//    @Id("selectedDates")
//    private Div selectedDates;
//
//    @Id("selected")
//    private Span selected;
//
    @Id("discount")
    private RadioButtonGroup<Discount> discountGroup;
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
    @Id("selected")
    private Span selected;
    @Id("service")
    private ComboBox<Service> serviceCb;
    @Id("unit")
    private ComboBox<Unit> unitCb;
    @Id("price")
    private ComboBox<PriceItem> priceCb;
    @Id("amount")
    private TextField amount;
    @Id("summ")
    private TextField sum;

    @Id("autosum")
    private Button autosum;

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

    private boolean hasChanges = false;
    private List<ScheduleDates> orderItemDates;



    /**
     * Creates a new OrderItemsEditor.
     */
    public OrderItemsEditor(ScheduleDateProvider provider, ScheduleDatesService datesService/*OrderItemPresenter presenter, ScheduleDateProvider provider, ScheduleDatesService datesService, PriceService priceService*/) {
        // You can initialise any data required for the connected UI components here.

//        this.presenter = presenter;
        this.datesService = datesService;
        this.dateProvider = provider;
//        this.priceService = priceService;
        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c -> {
        });

        heightField.setEnabled(false);
        setupDiscountGroup();
        setupGrid(provider);
        setAddButtonText("Добавить");

        serviceCb.setItems(Service.values());
        unitCb.setItems(Unit.values());
        unitCb.addValueChangeListener(e -> {
            List<PriceItem> priceItemList = getPriceItems(e.getValue(), serviceCb.getValue());
            priceCb.setItems(priceItemList);
        });

        binder.bind(priceCb, "price");
        priceCb.setRenderer(TemplateRenderer.<PriceItem> of(
                "<div>[[item.price]]<br><small>[[item.name]]</small></div>")
                .withProperty("price", i->FormattingUtils.formatAsCurrency(i.getPrice()) /*PriceItem::getPrice*/)
                .withProperty("name", PriceItem::getName));
        priceCb.setAllowCustomValue(false);
        priceCb.setRequired(true);
        //priceCb.setItemLabelGenerator(PriceItem::getName);

        amount.setRequired(true);
        amount.setRequiredIndicatorVisible(true);
        amount.setPattern("\\d+(\\,\\d?\\d?)?");
        amount.setPreventInvalidInput(true);
        amount.addValueChangeListener(e -> {
            if (!e.getValue().equals("")){
                setPrice();
            }
        });
        binder.forField(amount)
                .withConverter(new AmountConverter())
                .withValidator(i -> i > 0, "Количество должно быть больше 0")
                .bind("quantity");

        sum.setRequired(true);
        sum.setRequiredIndicatorVisible(true);
        binder.forField(sum).withConverter(new PriceConverter())
                .bind("totalPrice");

        autosum.addClickListener(e->{
            sum.setValue(autosum.getText());
        });
        autosum.setIcon(VaadinIcon.ARROW_LEFT.create());


//        widthField.addValueChangeListener(e-> heightField.setEnabled(true));
//        heightField.addValueChangeListener(e->
//                        amount.setValue(countAmount(Double.parseDouble(widthField.getValue()),
//                                Double.parseDouble(heightField.getValue()))));
//

//
        binder.bind(discountGroup, "discount");

        binder.bind(dates, "dates");


        add.addClickListener(e -> {
            if (currentPriceItem != null) {
                this.currentOrderItem.setPrice(currentPriceItem);
            }
            setValue(this.currentOrderItem);
            fireEvent(new SaveEvent(this, false));
        });
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, false)));

        cancel.addClickListener(e -> {
          //  setValue(this.currentOrderItem);
            closeSilently();
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



    private Double countAmount(double width, double height) {
        return width * height;
    }


    private void setPrice() {
        int oldPrice = totalPrice;
        Discount discount = discountGroup.getValue()!= null ? discountGroup.getValue() : Discount.none;

        String value = amount.getValue();
        if (value.equals("")) {
            value = "0";
        }
        String replace = value.replace(',', '.');
        double selectedAmount = Double.parseDouble(replace);
        PriceItem priceItem = priceCb.getValue();
        totalPrice = 0;
        int size = grid.getSelectionModel().getSelectedItems().size();

        if (priceItem != null && size != 0) {
            totalPrice = (int) (selectedAmount * priceItem.getPrice() * size);
        }
        switch (discount) {
            case none:
                break;
            case ten: totalPrice = totalPrice - (totalPrice*10/100); break;
            case twenty: totalPrice = totalPrice - (totalPrice*20/100); break;
            case thirty: totalPrice = totalPrice - (totalPrice*30/100); break;
        }
        autosum.setText(FormattingUtils.formatAsCurrency(totalPrice));
//        totalPriceDiv.setText(FormattingUtils.formatAsCurrency(totalPrice));
        if (oldPrice != totalPrice) {
            fireEvent(new PriceChangeEvent(this, oldPrice, totalPrice));
        }
    }


     private void setupGrid(ScheduleDateProvider provider) {
        grid.addColumn(new LocalDateRenderer<>(ScheduleDates::getDate,
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Дата").setWidth("70px").setFlexGrow(5);
        grid.addColumn(ScheduleDates::getWeekDay).setHeader("День недели").setWidth("70px").setFlexGrow(5);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.asMultiSelect()
                .addSelectionListener(event -> {
                    Set<ScheduleDates> selected = event.getAllSelectedItems();
                    dates.setValue(new ArrayList<>(selected));
                    setPrice();
                    this.selected.setText((String.format(
                            "Выбрано дат: %d",
                            selected.size())));
                });


//        grid.setItems(getDatesAfterCurrent(LocalDate.now()));
        provider.setFilter(new LocalDateToStringEncoder().encode(LocalDate.now()));
        grid.setDataProvider(provider);
       }

    private List<ScheduleDates> getDatesAfterCurrent(LocalDate date) {
        return datesService.findDatesAfterCurrent(date);
    }

    private void setupDiscountGroup() {
        discountGroup.setItems(Discount.none, Discount.ten, Discount.twenty, Discount.thirty);
        discountGroup.setValue(Discount.none);
        discountGroup.addValueChangeListener(e -> setPrice());
        discountGroup.setRenderer(new TextRenderer<>(Discount::toString));
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
        currentOrderItem = value;
        orderItemDates = value.getDates();
        orderItemDates.sort(Comparator.comparing(ScheduleDates::getDate));
        fieldSupport.setValue(value);
        binder.setBean(value);
        setPrice();
        hasChanges=false;

         if (orderItemDates.size() > 0) {
             List<ScheduleDates> temp = new ArrayList<>(orderItemDates);
             dates.setValue(orderItemDates);
             if (Collections.disjoint(getDatesAfterCurrent(LocalDate.now()), orderItemDates)){
                 temp.addAll(getDatesAfterCurrent(orderItemDates.get(orderItemDates.size()-1).getDate()));
                 grid.setItems(temp);
             } else if (temp.removeAll(getDatesAfterCurrent(LocalDate.now()))){
                 temp.addAll(getDatesAfterCurrent(LocalDate.now()));
                 grid.setItems(temp);
             } else{
                 grid.setItems(getDatesAfterCurrent(LocalDate.now()));
             }

             //!getDatesAfterCurrent(LocalDate.now()).containsAll(orderItemDates)

//             if (orderItemDates.get(orderItemDates.size()-1).getDate().isAfter(LocalDate.now())){
//                 temp.addAll(getDatesAfterCurrent(orderItemDates.get(orderItemDates.size()-1).getDate()));
//                 grid.setItems(temp);
//
//             }
             setGridData(orderItemDates);
         } else {
             grid.setItems(getDatesAfterCurrent(LocalDate.now()));
         }
    }




    public void clear() {
        binder.setBean(null);
    }

    public void write(OrderItem orderItem) throws ValidationException {
        binder.writeBean(orderItem);
    }

    public void close() {
        binder.readBean(null);
        grid.asMultiSelect().deselectAll();
        setTotalPrice(0);
    }

    public void closeSilently() {
        binder.readBean(null);
        grid.asMultiSelect().deselectAll();
        setTotalPrice(0);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<OrderItemsEditor, OrderItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    public void setCurrentPrice(Price defaultPrice) {
        this.defaultPrice = defaultPrice;
        priceCb.setItems(defaultPrice.getItemsPrice());
    }
    private void setGridData(List<ScheduleDates> datesSet) {
        grid.asMultiSelect().setValue(new HashSet<>(datesSet));
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

    public Stream<HasValue<?,?>> validate() {
        return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
    }

    private void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
        if (hasChanges) {
            fireEvent(new ValueChangeEventF(this));
        }
    }

    public boolean getHasChanges() {
        return hasChanges;
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



}
