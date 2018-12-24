package ru.xenya.market.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.backend.data.entity.Repayment;
import ru.xenya.market.ui.views.orderedit.OrderEditor;

public class DeleteRepaymentEvent extends ComponentEvent<Component> {

    private final Repayment payItem;

    public DeleteRepaymentEvent(Component source, Repayment payItem) {
        super(source, false);
        this.payItem = payItem;
    }

    public Repayment getPayItem() {
        return payItem;
    }
}
