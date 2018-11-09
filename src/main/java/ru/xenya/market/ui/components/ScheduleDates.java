package ru.xenya.market.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.service.ScheduleDatesService;

import java.time.LocalDate;

@Tag("schedule-dates")
@HtmlImport("src/components/schedule-dates.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

        LocalDate start = this.start.getValue();
        LocalDate end = this.end.getValue();

        createNewSchedule(start, end);
        clear();
    }

    private void createNewSchedule(LocalDate start, LocalDate end) {
        scheduleDatesService.createNew(start, end);
    }

    private void clear() {
        start.clear();
        end.clear();
        fireEvent(new ScheduleDatesEvent(this));
    }

    public class ScheduleDatesEvent extends ComponentEvent<ScheduleDates> {
        public ScheduleDatesEvent(ScheduleDates scheduleDates) {
            super(scheduleDates, false);
        }
    }

    public Registration addScheduleEventListener(ComponentEventListener<ScheduleDatesEvent> listener) {
        return addListener(ScheduleDatesEvent.class, listener);
    }


}
