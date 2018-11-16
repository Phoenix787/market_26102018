package ru.xenya.market.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.Invoice;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.backend.service.PriceService;

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
    private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);


    /**
     * Creates a new PriceItemField.
     * @param priceService
     */
    public PriceItemField(PriceService priceService) {
        // You can initialise any data required for the connected UI components here.
        //todo инициализировать все боксы и подставить это поле вместо всех полей прайса в OrderItemsEditor

        this.priceService = priceService;

        //todo послать событие
       // service.addValueChangeListener()
        service.setRenderer(TemplateRenderer.<Service> of("<div>[[item.service]]</div>")
                .withProperty("service", Service::toString));
        service.setDataProvider(DataProvider.ofItems(Service.values()));
        binder.bind(service, "price.service");

        //послать событие если изменения -> нужно в orderItemsEditor чтобы появились поля для заполнения см2,
        // это событие обрабатывать в orderItemsEditor назначить метод-обработчик
        unit.setItemLabelGenerator(Unit::toString);
        unit.setDataProvider(DataProvider.ofItems(Unit.values()));
        binder.bind(unit, "price.unit");


    }

    @Override
    public void setValue(PriceItem value) {

    }

    @Override
    public PriceItem getValue() {
        return null;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<PriceItemField, PriceItem>> listener) {
        return null;
    }

    /**
     * This model binds properties between PriceItemField and price-item-field.html
     */
    public interface PriceItemFieldModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }

    public void setPricePlan(Price pricePlan) {
        this.pricePlan = pricePlan;
    }
}
