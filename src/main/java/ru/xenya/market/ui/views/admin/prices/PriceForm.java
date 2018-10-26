package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView;

public class PriceForm implements CrudView.CrudForm<PriceItem> {



    @Override
    public FormButtonsBar getButtons() {
        return null;
    }

    @Override
    public HasText getTitle() {
        return null;
    }

    @Override
    public void setBinder(BeanValidationBinder<PriceItem> binder) {

    }
}
