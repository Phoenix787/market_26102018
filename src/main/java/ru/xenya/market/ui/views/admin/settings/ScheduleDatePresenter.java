package ru.xenya.market.ui.views.admin.settings;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.service.ScheduleDatesService;
import ru.xenya.market.ui.components.ScheduleDatesComponent;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScheduleDatePresenter {

    private SettingsView view;
    private final User currentUser;
    private final ScheduleDatesService service;

    public ScheduleDatePresenter(User currentUser, ScheduleDatesService service) {
        this.currentUser = currentUser;
        this.service = service;
    }

    public void init(SettingsView view) {
        this.view = view;
        view.getGrid().setItems(updateList());
       // view.getForm().addScheduleEventListener(e -> update());
    }

    private void update() {
        view.getOpenedEditor().setOpened(false);
        view.getGrid().setItems(updateList());
    }

    public List<ScheduleDates> updateList() {
        return service.findAll();
    }

    public Set<ScheduleDates> getDatesFromCurrent() {
          return service.findDatesAfterCurrent(LocalDate.now());
    }
}
