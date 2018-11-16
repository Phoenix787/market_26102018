package ru.xenya.market.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;


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

    @Id("pricePlan")
    private ComboBox<Price> pricePlan;
    @Id("service")
    private ComboBox<Service> service;
    @Id("unit")
    private ComboBox<Unit> unit;
    @Id("price")
    private ComboBox<PriceItem> price;


    /**
     * Creates a new PriceItemField.
     */
    public PriceItemField() {
        // You can initialise any data required for the connected UI components here.
        //todo инициализировать все боксы и подставить это поле вместо всех полей прайса в OrderItemsEditor

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
}
