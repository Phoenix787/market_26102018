package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.ui.views.admin.prices.events.NewEditorEvent;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PriceItemsEditor extends Div
        implements HasValueAndElement<ComponentValueChangeEvent<PriceItemsEditor,List<PriceItem>>, List<PriceItem>> {

   private PriceItemEditor empty;

    private boolean hasChanges = false;

    private final AbstractFieldSupport<PriceItemsEditor, List<PriceItem>> fieldSupport;

    private PriceItem currentPriceItem;

    public PriceItemsEditor() {

        this.fieldSupport = new AbstractFieldSupport<>(this, Collections.emptyList(),
                Objects::equals, c -> {
        });


    }

    public Stream<HasValue<?,?>> validate() {
        return getChildren()
                .filter((component -> fieldSupport.getValue().size() == 0 || !component.equals(empty)))
                .map(editor -> ((PriceItemEditor) editor).validate()).flatMap(stream -> stream);

    }

    @Override
    public void setValue(List<PriceItem> items) {
        fieldSupport.setValue(items);
        removeAll();
        hasChanges = false;

        if (items != null) {
            items.forEach(this::createEditor);
        }

        createEmptyElement();
        setHasChanges(false);
    }

    private void createEmptyElement() {
        empty = createEditor(null);
    }

    private PriceItemEditor createEditor(PriceItem value) {
        PriceItemEditor editor = new PriceItemEditor();
        getElement().appendChild(editor.getElement());
        editor.addServiceListener(e -> serviceChanged(e.getSource(), e.getService()));
        editor.addNameListener(e -> setHasChanges(true));
        editor.addUnitListener(e -> unitChanged(e.getSource(), e.getUnit()));
        editor.addDeleteListener(e->{
            PriceItemEditor priceItemEditor = e.getSource();
            if (priceItemEditor != empty) {
                remove(priceItemEditor);
                PriceItem priceItem = priceItemEditor.getValue();
                setValue(getValue().stream().filter(element-> element != priceItem).collect(Collectors.toList()));
                setHasChanges(true);
            }
        });

        editor.setValue(value);
        return editor;
    }

    private void unitChanged(PriceItemEditor item, Unit unit) {
        setHasChanges(true);
        if (empty == item) {
            currentPriceItem.setUnit(unit);
            item.setValue(currentPriceItem);
            setValue(Stream.concat(getValue().stream(), Stream.of(currentPriceItem)).collect(Collectors.toList()));
            fireEvent(new NewEditorEvent(this));
        }
    }

    private void serviceChanged(PriceItemEditor item, Service service) {
        setHasChanges(true);
        if (empty == item) {
            createEmptyElement();
            PriceItem priceItem = new PriceItem();
            this.currentPriceItem = priceItem;
            priceItem.setService(service);
            priceItem.setUnit(Unit.cm2);
            priceItem.setName("");
            priceItem.setPrice(0);
            item.setValue(priceItem);
            setValue(Stream.concat(getValue().stream(), Stream.of(priceItem)).collect(Collectors.toList()));
            fireEvent(new NewEditorEvent(this));
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        HasValueAndElement.super.setReadOnly(readOnly);
        getChildren().forEach(e->((PriceItemEditor)e).setReadOnly(readOnly));
    }

    @Override
    public List<PriceItem> getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ComponentValueChangeEvent<PriceItemsEditor, List<PriceItem>>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }


    public boolean hasChanges() {
        return hasChanges;
    }

    private void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
        if (hasChanges) {
            fireEvent(new ru.xenya.market.ui.views.admin.prices.events.ValueChangeEvent(this));
        }
    }
}
