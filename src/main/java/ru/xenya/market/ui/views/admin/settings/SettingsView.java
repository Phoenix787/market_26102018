package ru.xenya.market.ui.views.admin.settings;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.ui.components.ScheduleDates;

@Tag("settings-view")
@HtmlImport("src/views/admin/settings/settings-view.html")
public class SettingsView extends PolymerTemplate<TemplateModel> {

    @Id("scheduleButton")
    private Button scheduleButton;

    @Id("dialog")
    private Dialog dialog;

    private ScheduleDates scheduleDatesForm;

    public SettingsView() {

        scheduleButton.addClickListener(e -> openDialog());
    }

    private void openDialog() {
        dialog.add(scheduleDatesForm);
    }
}
