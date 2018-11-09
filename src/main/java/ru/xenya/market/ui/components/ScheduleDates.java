package ru.xenya.market.ui.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xenya.market.backend.service.ScheduleDatesService;

@Tag("schedule-dates")
@HtmlImport("src/components/schedule-dates.html")
public class ScheduleDates extends PolymerTemplate<TemplateModel> {

    @Id("start")
    private DatePicker start;
    @Id("end")
    private DatePicker end;
    @Id("save")
    private Button save;

    private final ScheduleDatesService scheduleDatesService;

    @Autowired
    public ScheduleDates(ScheduleDatesService scheduleDatesService) {
        this.scheduleDatesService = scheduleDatesService;

        save.addClickListener(e -> createPeriod());
    }

    private void createPeriod() {

    }
}
