package ru.xenya.market.ui.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.Invoice;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;

import java.util.Objects;
import java.util.stream.Stream;


/**
 * A Designer generated component for the price-item-field.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("price-item-field")
@HtmlImport("src/components/price-item-field.html")
public class PriceItemField extends PolymerTemplate<PriceItemField.PriceItemFieldModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<PriceItemField, PriceItem>, PriceItem> {

    //    @Id("pricePlan")
//    private ComboBox<Price> pricePlan;
    @Id("service")
    private ComboBox<Service> service;
    @Id("unit")
    private ComboBox<Unit> unit;
    @Id("price")
    private ComboBox<PriceItem> price;

    private final PriceService priceService;
    private Price pricePlan;
    private BeanValidationBinder<OrderItem> binder;
    private PriceItem currentPriceItem;
    private final AbstractFieldSupport<PriceItemField,PriceItem> fieldSupport;


    /**
     * Creates a new PriceItemField.
     * @param priceService
     */
    public PriceItemField(PriceService priceService) {
        // You can initialise any data required for the connected UI components here.
        fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, c ->  {});


        this.priceService = priceService;
        pricePlan = priceService.getRepository().findByDefaultPrice(true);

        service.addValueChangeListener(e->{
            getModel().setService(DataProviderUtils.convertIfNotNull(e.getValue(), Service::name));
            fireEvent(new ServiceChangedEvent(this, e.getValue()));
        });
        service.setRenderer(TemplateRenderer.<Service> of("<div>[[item.service]]</div>")
                .withProperty("service", Service::toString));
        service.setDataProvider(DataProvider.ofItems(Service.values()));
//            binder.bind(service, "price.service");

        //послать событие если изменения -> нужно в orderItemsEditor чтобы появились поля для заполнения см2,
        // это событие обрабатывать в orderItemsEditor назначить метод-обработчик
        unit.setItemLabelGenerator(Unit::toString);
        unit.setDataProvider(DataProvider.ofItems(Unit.values()));
        unit.addValueChangeListener(event -> {
            Unit unit = event.getValue();
            Service service = this.service.getValue();
            price.setItems(priceService.getPriceItems(pricePlan, service, unit));
            price.setEnabled(unit != null);
            getModel().setUnit(DataProviderUtils.convertIfNotNull(event.getValue(), Unit::name));
            fireEvent(new UnitChangedEvent(this, event.getValue()));
        });
//         binder.bind(unit, "price.unit");

        price.setRenderer(TemplateRenderer.<PriceItem> of(
                "<div>[[item.price]]<br><small>[[item.name]]</small></div>")
                .withProperty("price", PriceItem::getPrice)
                .withProperty("name", PriceItem::getName));
        price.setItemLabelGenerator(PriceItem::getName);
        price.addValueChangeListener(e->{
            // по идее должен быть уже сформированный priceItem который мы и передаем в едитор
            fieldSupport.setValue(e.getValue());
            fireEvent(new PriceChangedEvent(this, e.getValue()));

        });
        //binder.bind(price, "price.price");
//
//        binder.forField(price)
//                .bind(OrderItem::getPrice, OrderItem::setPrice);



    }

    @Override
    public void setValue(PriceItem value) {
        fieldSupport.setValue(value);
        currentPriceItem = value;
//        boolean noServiceSelected = value == null || value.getService() == null;
//        unit.setEnabled(!noServiceSelected);
//        price.setEnabled(!noServiceSelected);

    }

    @Override
    public PriceItem getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<PriceItemField, PriceItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    public PriceItem getCurrentPriceItem() {
        return currentPriceItem;
    }

    public void setCurrentPriceItem(PriceItem currentPriceItem) {
        this.currentPriceItem = currentPriceItem;
    }

    public void hasChange(boolean b) {
        unit.setEnabled(b);
        price.setEnabled(b);
    }

    /**
     * This model binds properties between PriceItemField and price-item-field.html
     */
    public interface PriceItemFieldModel extends TemplateModel {

        void setService(String service);

        void setUnit(String unit);

        // Add setters and getters for template properties here.
    }


    public void setPricePlan(Price pricePlan) {
        this.pricePlan = pricePlan;
    }

    public void setBinder(BeanValidationBinder<OrderItem> binder) {
        this.binder = binder;


    }

    public class ServiceChangedEvent extends ComponentEvent<PriceItemField> {
        private final Service service;

        public ServiceChangedEvent(PriceItemField source, Service value) {
            super(source, false);
            this.service = value;
        }

        public Service getService() {
            return service;
        }
    }

    public class UnitChangedEvent extends ComponentEvent<PriceItemField> {
        private final Unit unit;

        public UnitChangedEvent(PriceItemField source, Unit value) {
            super(source, false);
            this.unit = value;
        }

        public Unit getUnit() {
            return unit;
        }
    }

    public class PriceChangedEvent extends ComponentEvent<PriceItemField> {
        private final PriceItem priceItem;
        public PriceChangedEvent(PriceItemField source, PriceItem value) {
            super(source, false);
            this.priceItem = value;
        }

        public PriceItem getPriceItem() {
            return priceItem;
        }
    }

    public Registration addServiceChangeListener(ComponentEventListener<ServiceChangedEvent> listener) {
        return addListener(ServiceChangedEvent.class, listener);
    }
    public Registration addUnitChangeListener(ComponentEventListener<UnitChangedEvent> listener) {
        return addListener(UnitChangedEvent.class, listener);
    }
    public Registration addPriceChangeListener(ComponentEventListener<PriceChangedEvent> listener) {
        return addListener(PriceChangedEvent.class, listener);
    }
}


