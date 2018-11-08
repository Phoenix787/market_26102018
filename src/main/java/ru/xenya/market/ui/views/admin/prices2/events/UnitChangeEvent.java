package ru.xenya.market.ui.views.admin.prices2.events;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.ui.views.admin.prices.PriceItemEditor;

public class UnitChangeEvent extends ComponentEvent<PriceItemEditor> {

    private final Unit unit;
    public UnitChangeEvent(PriceItemEditor component, Unit unit) {
        super(component, false);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
}
