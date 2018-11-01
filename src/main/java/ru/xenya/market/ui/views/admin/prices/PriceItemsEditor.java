package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import ru.xenya.market.backend.data.entity.PriceItem;

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
