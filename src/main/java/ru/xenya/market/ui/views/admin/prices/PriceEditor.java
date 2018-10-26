package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.SaveEvent;

import java.util.stream.Stream;

/**
 * посмотреть как сделано в ordereditor bakery про добавление позиций... сделать так же
 */

@Tag("price-editor")
@HtmlImport("src/views/admin/prices/price-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PriceEditor extends PolymerTemplate<TemplateModel> {//implements CrudForm<Price> {


    @Id("title")
    private H3 title;

    @Id("date")
    private DatePicker date;

    @Id("itemsContainer")
    private Div itemsContainer;

    @Id("priceNumber")
    private Span priceNumber;

    private PriceItemsEditor itemsEditor;

    private User currentUser;

    private Price currentPrice;

    private FormButtonsBar buttons;

    BeanValidationBinder<Price> binder = new BeanValidationBinder<>(Price.class);

    public PriceEditor() {
      //  itemsEditor = new PriceItemsEditor();
      //  itemsContainer.add(itemsEditor);

    }

    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public boolean hasChanges() {
        return binder.hasChanges();
    }

    public void clear() {
        binder.readBean(null);
    }

    public void write(Price price) throws ValidationException {
        binder.writeBean(price);
    }

    public void close() {

    }

    public void read(Price price, boolean isNew) {
        binder.readBean(price);

        this.priceNumber.setText(isNew ? "" : price.getId().toString());
        title.setVisible(isNew);

    }

//    public Stream<HasValue<?, ?>> validate() {
//        Stream<HasValue<?, ?>> errorFields =
//                binder.validate().getFieldValidationErrors().stream()
//                .map(BindingValidationStatus::getField);
//        return Stream.concat(errorFields, itemsEditor.validate());
//    }
}
