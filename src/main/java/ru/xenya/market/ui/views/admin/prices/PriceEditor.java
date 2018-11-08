package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.DeleteEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.views.admin.prices.events.ValueChangeEvent;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

@Tag("price-editor")
@HtmlImport("src/views/admin/prices/price-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PriceEditor extends PolymerTemplate<TemplateModel> {


    @Id("title")
    private H3 title;

    @Id("metaContainer")
    private Div metaContainer;

    @Id("date")
    private DatePicker date;

    @Id("checkbox")
    private Checkbox isDefault;

    @Id("priceNumber")
    private Span priceNumber;

    @Id("cancel")
    private Button cancelButton;

    @Id("confirm")
    private Button confirmButton;

    @Id("delete")
    private Button deleteButton;

    @Id("itemsContainer")
    private Div itemsContainer;

    private PriceItemsEditor itemsEditor;

    private User currentUser;

    private Price currentPrice;

    BeanValidationBinder<Price> binder = new BeanValidationBinder<>(Price.class);

    @Autowired
    public PriceEditor() {
        itemsEditor = new PriceItemsEditor();

        itemsContainer.add(itemsEditor);

        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        confirmButton.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, false)));

        date.setI18n(new DatePicker.DatePickerI18n()
                .setWeek("неделя")
                .setCalendar("календарь")
                .setToday("сегодня")
                .setCancel("отмена")
                .setFirstDayOfWeek(1)
        .setMonthNames(Arrays.asList("январь", "февраль", "март",
                "апрель", "май", "июнь",
                "июль", "август", "сентябрь",
                "октябрь", "ноябрь", "декабрь"))
                .setWeekdays(
                        Arrays.asList("воскресенье", "понедельник", "вторник",
                        "среда", "четверг", "пятница", "суббота"))
        .setWeekdaysShort(Arrays.asList("вс", "пн", "вт", "ср", "чт", "пт", "сб")));
        date.setLocale(Locale.UK);
        date.setRequired(true);
        binder.bind(date, Price::getDate, Price::setDate);

        isDefault.addValueChangeListener(event->{

                confirmButton.setEnabled(true);

        });

        binder.bind(isDefault, Price::isDefaultPrice, Price::setDefaultPrice);


        itemsEditor.setRequiredIndicatorVisible(true);
        binder.bind(itemsEditor, "itemsPrice");

        ComponentUtil.addListener(itemsEditor, ValueChangeEvent.class, e -> confirmButton.setEnabled(hasChanges()));

//        binder.addValueChangeListener(e->{
//            if (e.getOldValue() != null) {
//                confirmButton.setEnabled(hasChanges());
//            }
//        });

    }

    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Price getCurrentPrice() { return currentPrice; }


//    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
//        return cancelButton.addClickListener(
//                e->listener.onComponentEvent(new CancelEvent(this, true)));
//    }
//
//    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener){
//        return confirmButton.addClickListener
//                (e -> listener.onComponentEvent(new SaveEvent(this, true)));
//    }
//
//
//    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
//        return deleteButton.addClickListener(e->
//        listener.onComponentEvent(new DeleteEvent(this, true)));
//
//    }


    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(
                CancelEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener){
        return addListener
                (SaveEvent.class, listener);
    }


    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);

    }

    public boolean hasChanges() {
        return binder.hasChanges() || itemsEditor.hasChanges();
    }

    public void clear()
    {
        binder.readBean(null);
        itemsEditor.setValue(null);
    }

    public void write(Price price) throws ValidationException {
        binder.writeBean(price);
    }

    public void close() {

    }

    public void read(Price price, boolean isNew) {
//        binder.readBean(price);
        binder.setBean(price);

        this.priceNumber.setText(isNew ? "" : price.getId().toString());
        title.setVisible(isNew);
        metaContainer.setVisible(!isNew);

        confirmButton.setEnabled(false);

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    public Stream<HasValue<?, ?>> validate() {
        Stream<HasValue<?, ?>> errorFields =
                binder.validate().getFieldValidationErrors().stream()
                .map(BindingValidationStatus::getField);
        return Stream.concat(errorFields, itemsEditor.validate());
    }
}
