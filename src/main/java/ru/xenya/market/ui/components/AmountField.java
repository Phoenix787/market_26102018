package ru.xenya.market.ui.components;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the amount-field.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("amount-field")
@HtmlImport("src/components/amount-field.html")
public class AmountField extends AbstractSinglePropertyField<AmountField, Double> {

    /**
     * Creates a new AmountField.
     */
    public AmountField() {
        // You can initialise any data required for the connected UI components here.
        super("value", null, true);
    }

    public void setEnabled(boolean enabled){
        getElement().setProperty("disabled", !enabled);
    }

    public void setMin(double value){
        getElement().setProperty("min", value);
    }
    public void setMax(double value) {
        getElement().setProperty("max", value);
    }

    public void setEditable(boolean editable){
        getElement().setProperty("editable", editable);
    }

    public void setPattern(String pattern) {
        getElement().setProperty("pattern", pattern);
    }


}
