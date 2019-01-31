package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import ru.xenya.market.app.HasLogger;
import ru.xenya.market.backend.data.Role;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.components.common.ConfirmDialog;
import ru.xenya.market.ui.components.common.ConfirmationDialog;
import ru.xenya.market.ui.views.EntityView;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static ru.xenya.market.ui.utils.MarketConst.*;

// это как сustomerviewtemplate

@Tag("prices-view")
@HtmlImport("src/views/admin/prices/prices-view.html")
@Route(value = PAGE_PRODUCTS, layout = MainView.class)
@StyleSheet("frontend://styles/styles.css")
@PageTitle(TITLE_PRODUCTS)
@Secured(Role.ADMIN)

public class PricesView extends PolymerTemplate<TemplateModel>
        implements HasLogger, HasUrlParameter<Long>, EntityView<Price> {

    @Id("search")
    private SearchBar search;

    @Id("grid")
    private Grid<Price> grid;

    @Id("dialog")
    private Dialog dialog;

    private ConfirmDialog confirmation;

    private final PriceEditor priceEditor;

    private final PricePresenter presenter;

    @Autowired
    public PricesView(PriceEditor priceEditor, PricePresenter presenter) {

        this.priceEditor = priceEditor;
        this.presenter = presenter;
        this.presenter.init(this);

        //для того, чтобы открывающееся окно было высотой 100% и прижималось к правому краю
        dialog.add((Component) getForm());
        dialog.setHeight("100%");
        dialog.getElement().addAttachListener(event -> UI.getCurrent().getPage().executeJavaScript(
                "$0.$.overlay.setAttribute('theme', 'right');", dialog.getElement()
        ));

        setupGrid();
        setupListeners();
    }


    private void setupListeners() {

        grid.addSelectionListener(e->{
            e.getFirstSelectedItem().ifPresent(entity->{
                System.err.println(entity);

                presenter.onNavigation(entity.getId(), true);
                getGrid().deselectAll();
            });
        });

        search.setActionText("Новый");
        search.setPlaceHolder("Поиск");
        search.addActionClickListener(e->presenter.createNewPrice());
        search.addFilterChangeListener(e->presenter.filter(search.getFilter()));

        dialog.getElement().addEventListener("opened-changed", e->{
            if (!dialog.isOpened()){
                presenter.cancel();
            }
        });
       // confirmation.addCancelListener(e->{setOpened(true); confirmation.setOpened(false);});
    }

    private void setupGrid() {
        grid.addColumn(Price::getId).setHeader("№").setWidth("50px").setFlexGrow(10);
        grid.addColumn(new LocalDateRenderer<>(Price::getDate,
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Дата")
                .setWidth("100px")
                .setFlexGrow(10);
        grid.addColumn(new ComponentRenderer<>(Div::new,
                (div, price) -> div.setText(price.isDefaultPrice() ? "По умолчанию" : ""))).setHeader("Статус")
        .setWidth("100px").setFlexGrow(5);
       // grid.setSelectionMode(Grid.SelectionMode.NONE);

    }

    public void setOpened(boolean opened) {
        dialog.setOpened(opened);
    }



    SearchBar getSearchBar() {
        return search;
    }

    public PriceEditor getOpenedEditor(){
        return priceEditor;
    }



    public Grid<Price> getGrid() {
        return grid;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long idPrice) {
      //  boolean editView = event.getLocation().getPath().contains(PAGE_PRODUCTS_EDIT);
        if (idPrice != null) {
            presenter.onNavigation(idPrice, true);
        } else if (dialog.isOpened()) {
            presenter.closeSilently();
        }
    }

    void navigateToMainView() {
        getUI().ifPresent(ui -> ui.navigate(PAGE_PRODUCTS));
    }

    @Override
    public boolean isDirty() {
        return priceEditor.hasChanges();
    }

    @Override
    public void clear() {
        priceEditor.clear();
    }

    @Override
    public void write(Price entity) throws ValidationException {
        priceEditor.write(entity);
    }

//    public Stream<HasValue<?,?>> validate(){
//        return priceEditor.validate();
//    }


    @Override
    public String getEntityName() {
        return EntityUtil.getName(Price.class);
    }

    @Override
    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmation = confirmDialog;
    }

    @Override
    public ConfirmDialog getConfirmDialog() {
        return confirmation;
    }

    public void setDialogElementsVisibility(boolean editing) {
        dialog.add(priceEditor);
        priceEditor.setVisible(editing);
    }

    public PriceEditor getForm() {
        return priceEditor;
    }
}
