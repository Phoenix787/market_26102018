package ru.xenya.market.ui.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;

import java.util.List;
import java.util.Objects;


/**
 * A Designer generated component for the priceComboBox-item-field.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("price-item-field")
@HtmlImport("src/components/price-item-field.html")
public class PriceItemField extends PolymerTemplate<PriceItemField.PriceItemFieldModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<PriceItemField, PriceItem>, PriceItem> {

    @Id("service")
    private ComboBox<Service> service;
    @Id("unit")
    private ComboBox<Unit> unit;
    @Id("price")
    private ComboBox<PriceItem> priceComboBox;

    private final PriceService priceService;
    private Price pricePlan;
    private BeanValidationBinder<OrderItem> binder;
    private BeanValidationBinder<PriceItem> binderP = new BeanValidationBinder<>(PriceItem.class);
    private OrderItem currentOrderItem;
    private PriceItem currentPriceItem;
    private final AbstractFieldSupport<PriceItemField,PriceItem> fieldSupport;
    private List<PriceItem> priceItems;


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
        priceItems = pricePlan.getItemsPrice();

        service.addValueChangeListener(e->{
            getModel().setService(DataProviderUtils.convertIfNotNull(e.getValue(), Service::name));
            fireEvent(new ServiceChangedEvent(this, e.getValue()));
        });
        service.setRenderer(TemplateRenderer.<Service> of("<div>[[item.service]]</div>")
                .withProperty("service", Service::toString));
        service.setDataProvider(DataProvider.ofItems(Service.values()));

        unit.setItemLabelGenerator(Unit::toString);
        unit.setDataProvider(DataProvider.ofItems(Unit.values()));
//        unit.addValueChangeListener(event -> {
//            Unit unit = event.getValue();
//            Service service = this.service.getValue();
//            priceComboBox.setItems(priceService.getPriceItems(pricePlan, service, unit));
//            priceComboBox.setEnabled(unit != null);
//            getModel().setUnit(DataProviderUtils.convertIfNotNull(event.getValue(), Unit::name));
//
//            fireEvent(new UnitChangedEvent(this, event.getValue()));
//        });
        priceComboBox.setRenderer(TemplateRenderer.<PriceItem> of(
                "<div>[[item.priceComboBox]]<br><small>[[item.name]]</small></div>")
                .withProperty("priceComboBox", PriceItem::getPrice)
                .withProperty("name", PriceItem::getName));
        priceComboBox.setItems(priceItems);
     //   priceComboBox.setItemLabelGenerator(PriceItem::getName);
        priceComboBox.addValueChangeListener(e->{
            // по идее должен быть уже сформированный priceItem который мы и передаем в едитор
          //  fieldSupport.setValue(e.getValue());
            fireEvent(new PriceChangedEvent(this, e.getValue()));

        });


    }
//todo с установкой значений в комбобокс
    @Override
    public void setValue(PriceItem value) {
        fieldSupport.setValue(value);
        currentPriceItem = value;
//
       // binder.setBean(currentOrderItem);
//
//        boolean isValue = value != null && currentOrderItem != null;
//        if (!isValue){
//            service.setValue(null);
//            unit.setValue(null);
//            priceComboBox.setItems(priceItems);
////            priceComboBox.setValue(null);
//        }
        //else {
          //  hasChange(true);
//            priceComboBox.setItems(priceService.getPriceItems(pricePlan, value.getService(), value.getUnit()));
      //  }
//        boolean noServiceSelected = value == null || value.getService() == null;
//        unit.setEnabled(!noServiceSelected);
//        priceComboBox.setEnabled(!noServiceSelected);

    }

    public void setCurrentOrderItem(OrderItem currentOrderItem) {
        this.currentOrderItem = currentOrderItem;
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
        priceComboBox.setItems(priceService.getPriceItems(pricePlan, currentPriceItem.getService(), currentPriceItem.getUnit()));
    }

    /**
     * This model binds properties between PriceItemField and priceComboBox-item-field.html
     */
    public interface PriceItemFieldModel extends TemplateModel {

        void setService(String service);

        void setUnit(String unit);

        void setPrice(String price);

        // Add setters and getters for template properties here.
    }


    public void setPricePlan(Price pricePlan) {
        this.pricePlan = pricePlan;

//        unit.addValueChangeListener(event -> {
//            Unit unit = event.getValue();
//            Service service = this.service.getValue();
//            priceComboBox.setItems(priceService.getPriceItems(pricePlan, service, unit));
//            priceComboBox.setEnabled(unit != null);
//            getModel().setUnit(DataProviderUtils.convertIfNotNull(event.getValue(), Unit::name));
//
//            fireEvent(new UnitChangedEvent(this, event.getValue()));
//        });

    }
//todo что-то не так с установкой значений из биндера
    public void setBinder(BeanValidationBinder<OrderItem> binder) {
        this.binder = binder;

        binder.forField(service).bind(OrderItem::getService, OrderItem::setService);
        binder.forField(unit).bind(OrderItem::getUnit, OrderItem::setUnit);
        binder.forField(priceComboBox).bind(OrderItem::getPrice, OrderItem::setPrice);




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


