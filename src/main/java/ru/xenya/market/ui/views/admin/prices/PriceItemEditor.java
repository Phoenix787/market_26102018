package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;
import ru.xenya.market.ui.utils.converters.ServiceConverter;
import ru.xenya.market.ui.views.admin.prices.events.DeleteEvent;
import ru.xenya.market.ui.views.admin.prices.events.NameChangeEvent;
import ru.xenya.market.ui.views.admin.prices.events.PriceChangeEvent;
import ru.xenya.market.ui.views.admin.prices.events.UnitChangeEvent;
import ru.xenya.market.ui.views.admin.prices.utils.PriceConverter;

import java.util.Objects;
import java.util.stream.Stream;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.createItemLabelGenerator;


@Tag("price-item-editor")
@HtmlImport("src/views/admin/prices/price-item-editor.html")
public class PriceItemEditor extends PolymerTemplate<PriceItemEditor.Model>
    implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<PriceItemEditor, PriceItem>, PriceItem> {



    public interface Model extends TemplateModel{

        void setService(String service);

        void setUnit(String unit);

        String getService();
    }

    @Id("services")
    private ComboBox<Service> service;

    @Id("units")
    private ComboBox<Unit> unit;

    @Id("name")
    private TextField name;

    @Id("price")
    private TextField price;

    @Id("delete")
    private Button delete;

    private final AbstractFieldSupport<PriceItemEditor, PriceItem> fieldSupport;

    private BeanValidationBinder<PriceItem> binder = new BeanValidationBinder<>(PriceItem.class);

    public PriceItemEditor() {
        this.fieldSupport = new AbstractFieldSupport<>(this, null, Objects::equals, c->{});
        service.setItemLabelGenerator(createItemLabelGenerator(Service::toString));
        service.setDataProvider(DataProvider.ofItems(Service.values()));
        service.addValueChangeListener(e -> {
            getModel().setService(DataProviderUtils.convertIfNotNull(e.getValue(), Service::name));
            fireEvent(new PriceChangeEvent(this, e.getValue()));
        });

        unit.setItemLabelGenerator(createItemLabelGenerator(Unit::toString));
        unit.setDataProvider(DataProvider.ofItems(Unit.values()));
        unit.addValueChangeListener(e -> {
                    getModel().setUnit(DataProviderUtils.convertIfNotNull(e.getValue(), Unit::name));
                    fireEvent(new UnitChangeEvent(this, e.getValue()));
        });

        binder.forField(service)
               .withValidator(new BeanValidator(PriceItem.class, "service"))
                .bind(PriceItem::getService,PriceItem::changeService);


        binder.forField(unit)
                .withValidator(new BeanValidator(PriceItem.class, "unit"))
        .bind(PriceItem::getUnit, (o, s)->o.changeUnit(s));

        name.addValueChangeListener(e -> fireEvent(new NameChangeEvent(this, e.getValue())));
        binder.bind(name, "name");
//        binder.forField(price).withConverter(new StringToIntegerConverter("must be number"))
//                .bind(PriceItem::getPrice, PriceItem::setPrice);

        binder.forField(price).withConverter(new PriceConverter())
                .bind(PriceItem::getPrice, PriceItem::setPrice);
        price.setPattern("\\d+(\\.\\d?\\d?)?$");
        price.setPreventInvalidInput(true);
       // binder.bind(price, "price");

        service.setRequired(true);
        unit.setRequired(true);
        name.setRequired(true);
        price.setRequired(true);

        delete.addClickListener(e->fireEvent(new DeleteEvent(this)));
    }

    @Override
    public void setValue(PriceItem value) {
        fieldSupport.setValue(value);
        boolean noServiceSelected = value == null || value.getService() == null;
//        boolean noUnitSelected = value.getUnit() == null;
//        if (!noUnitSelected){
            binder.setBean(value);
      //  }
        unit.setEnabled(!noServiceSelected);
        delete.setEnabled(!noServiceSelected);
        name.setEnabled(!noServiceSelected);

    }

    @Override
    public PriceItem getValue() {
        return fieldSupport.getValue();
    }

    public Stream<HasValue<?,?>> validate(){
        return binder.validate().getFieldValidationErrors().stream()
                .map(BindingValidationStatus::getField);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addServiceListener(ComponentEventListener<PriceChangeEvent> listener) {
        return addListener(PriceChangeEvent.class, listener);
    }


    public Registration addNameListener(ComponentEventListener<NameChangeEvent> listener) {
        return addListener(NameChangeEvent.class, listener);
    }

    public Registration addUnitListener(ComponentEventListener<UnitChangeEvent> listener) {
        return addListener(UnitChangeEvent.class, listener);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<PriceItemEditor, PriceItem>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

}
