package ru.xenya.market.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.service.ScheduleDatesService;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Designer generated component for the selected-dates.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("selected-dates")
@HtmlImport("src/components/selected-dates.html")
public class SelectedDates extends PolymerTemplate<SelectedDates.SelectedDatesModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<SelectedDates, List<ScheduleDates>>, List<ScheduleDates>> {

    private final ScheduleDatesService datesService;

    private List<ScheduleDates> currentDates;

    @Id("div")
    private Div div;

    private final AbstractFieldSupport<SelectedDates, List<ScheduleDates>> fieldSupport;
    /*@Id("grid")
    private Grid<ScheduleDates> grid;
*/
    /**
     * Creates a new SelectedDates.
     * @param datesService
     */
    public SelectedDates(ScheduleDatesService datesService) {
        this.fieldSupport = new AbstractFieldSupport<>(this, Collections.emptyList(), Objects::equals, c ->  {});
        this.datesService = datesService;
        div.setHeight("30vh");
        // You can initialise any data required for the connected UI components here.
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);
//        grid.addColumn(ScheduleDates::getDate);
//        grid.setItems(datesService.findDatesAfterCurrent(LocalDate.now()));
    }

    @Override
    public void setValue(List<ScheduleDates> value) {
        fieldSupport.setValue(value);
        currentDates = value;
        if (value != null) {
            div.setText(currentDates.stream()
                    .map(ScheduleDates::getDate)
                    .sorted((Comparator.naturalOrder()))
                    .map(e->e.format(FormattingUtils.FULL_DAY_FORMATTER))
                    .collect(Collectors.joining(", ")));
        }

//        if (currentDates != null && currentDates.size() > 0){
//              temp = datesService.findDatesAfterCurrent(currentDates.get(currentDates.size()-1).getDate());
//               currentDates.addAll(temp);
//            grid.setItems(currentDates);
//            for (ScheduleDates date : value) {
//                grid.select(date);
//            }
//            div.setText(value.stream()
//                    .map(ScheduleDates::getDate)
//                    .map(e->e.format(DateTimeFormatter.ISO_LOCAL_DATE))
//                    .collect(Collectors.joining(", ")));
//        }else{
//            currentDates = datesService.findDatesAfterCurrent(LocalDate.now());
//            grid.setItems(currentDates);
//        }


    }

    @Override
    public List<ScheduleDates> getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SelectedDates, List<ScheduleDates>>> listener) {
        return fieldSupport.addValueChangeListener(listener);
               // grid.asMultiSelect().addValueChangeListener((ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Grid<ScheduleDates>, Set<ScheduleDates>>>) listener);
    }


    /**
     * This model binds properties between SelectedDates and selected-dates.html
     */
    public interface SelectedDatesModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
