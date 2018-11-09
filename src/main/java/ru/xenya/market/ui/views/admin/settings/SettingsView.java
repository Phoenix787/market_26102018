package ru.xenya.market.ui.views.admin.settings;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.security.access.annotation.Secured;
import ru.xenya.market.backend.data.Role;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.ScheduleDates;
import ru.xenya.market.ui.utils.MarketConst;

import static ru.xenya.market.ui.utils.MarketConst.PAGE_SETTINGS;

@Tag("settings-view")
@HtmlImport("src/views/admin/settings/settings-view.html")
@Route(value = PAGE_SETTINGS, layout = MainView.class)
@PageTitle(MarketConst.TITLE_SETTINGS)
@Secured(Role.ADMIN)
public class SettingsView extends PolymerTemplate<TemplateModel> {

    @Id("scheduleButton")
    private Button scheduleButton;

    @Id("dialog")
    private Dialog dialog;

    private ScheduleDates scheduleDatesForm;

    public SettingsView(ScheduleDates scheduleDatesForm) {
        this.scheduleDatesForm = scheduleDatesForm;

        this.scheduleButton.addClickListener(e -> openDialog());

        scheduleDatesForm.addScheduleEventListener(e -> close());
    }

    private void openDialog() {
        dialog.add(scheduleDatesForm);
        dialog.setOpened(true);
    }

    public ScheduleDates getForm() {
        return scheduleDatesForm;
    }

    private void close() {
        dialog.setOpened(false);
    }
}
