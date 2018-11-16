package ru.xenya.market.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.service.ScheduleDatesService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    @Id("grid")
    private Grid<ScheduleDates> grid;

    /**
     * Creates a new SelectedDates.
     * @param datesService
     */
    public SelectedDates(ScheduleDatesService datesService) {
        this.datesService = datesService;
        // You can initialise any data required for the connected UI components here.
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(ScheduleDates::getDate);
        grid.setItems(datesService.findDatesAfterCurrent(LocalDate.now()));
    }

    @Override
    public void setValue(List<ScheduleDates> value) {
        currentDates = value;
        List<ScheduleDates> temp = datesService.findDatesAfterCurrent(currentDates.get(currentDates.size()-1).getDate());
        currentDates.addAll(temp);
        grid.setItems(currentDates);
        for (ScheduleDates date : value) {
            grid.select(date);
        }
        div.setText(value.stream()
                    .map(ScheduleDates::getDate)
                    .map(e->e.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    .collect(Collectors.joining(", ")));
    }

    @Override
    public List<ScheduleDates> getValue() {
//        List<String> temp = Stream.of(div.getText().split(",")).collect(Collectors.toList());
//        List<LocalDate> result = temp.stream().map(LocalDate::parse).collect(Collectors.toList());
////найти объекты в dateservice и поместить из в результирующий list<scheduledates>
        return new ArrayList<>(grid.asMultiSelect().getValue());
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SelectedDates, List<ScheduleDates>>> listener) {
        return grid.asMultiSelect().addValueChangeListener((ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Grid<ScheduleDates>, Set<ScheduleDates>>>) listener);
    }


    /**
     * This model binds properties between SelectedDates and selected-dates.html
     */
    public interface SelectedDatesModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
