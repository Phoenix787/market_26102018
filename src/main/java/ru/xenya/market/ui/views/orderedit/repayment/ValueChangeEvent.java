package ru.xenya.market.ui.views.orderedit.repayment;

import com.vaadin.flow.component.ComponentEvent;
import ru.xenya.market.ui.views.orderedit.orderitem.OrderItemsView;

public class ValueChangeEvent extends ComponentEvent<RepaymentView> {

    public ValueChangeEvent(RepaymentView source) {
        super(source, false);
    }
}
