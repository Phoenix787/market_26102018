package ru.xenya.market.ui.views.admin.settings;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.security.access.annotation.Secured;
import ru.xenya.market.backend.data.Role;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.ScheduleDatesComponent;
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

    @Id("grid")
    private Grid<ScheduleDates> grid;

    private final ScheduleDatesComponent scheduleDatesForm;

    private final ScheduleDatePresenter presenter;

    public SettingsView(ScheduleDatesComponent scheduleDatesForm, ScheduleDatePresenter presenter) {
        this.scheduleDatesForm = scheduleDatesForm;
        this.presenter = presenter;
        presenter.init(this);
        this.scheduleButton.addClickListener(e -> openDialog());
        this.scheduleDatesForm.addScheduleEventListener(e -> close());

        dialog.add(getForm());

        setupGrid();


    }

    private void setupGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(ScheduleDates::getId).setWidth("70px").setHeader("№").setFlexGrow(0);
        grid.addColumn(ScheduleDates::getDate).setWidth("70px").setHeader("Дата");
        grid.addColumn(ScheduleDates::getWeekDay).setWidth("100px").setHeader("День недели").setFlexGrow(5);

    }

    private void openDialog() {
        dialog.setOpened(true);
    }

    public ScheduleDatesComponent getForm() {
        return scheduleDatesForm;
    }

    private void close() {
        grid.setItems(presenter.updateList());
        dialog.setOpened(false);
    }

    public Grid<ScheduleDates> getGrid() {
        return grid;
    }

    public Dialog getOpenedEditor(){
        return dialog;
    }

}
