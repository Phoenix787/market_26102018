package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xenya.market.app.HasLogger;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.components.common.ConfirmationDialog;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.views.EntityView;

import java.util.stream.Stream;

import static ru.xenya.market.ui.utils.MarketConst.*;

// это как сustomerviewtemplate

//todo почему не отображается ни поиск ни таблица???
@Tag("prices-view")
@HtmlImport("src/views/admin/prices/prices-view.html")
@Route(value = PAGE_PRODUCTS, layout = MainView.class)
@PageTitle(TITLE_PRODUCTS)

//может сделать  CrudView<Customer, TemplateModel>
public class PricesView extends PolymerTemplate<TemplateModel>
        implements HasLogger, HasUrlParameter<Long>, EntityView<Price> {

    @Id("search")
    private SearchBar search;

    @Id("grid")
    private Grid<Price> grid;

    @Id("dialog")
    private Dialog dialog;

    private ConfirmationDialog<Price> confirmation;

    private final PriceEditor priceEditor;

    private final PricePresenter presenter;

    @Autowired
    public PricesView(PriceEditor priceEditor, PricePresenter presenter) {
        this.priceEditor = priceEditor;
        this.presenter = presenter;

        search.setActionText("Новый прайс");
        search.setPlaceHolder("Поиск");

        setupGrid();


        presenter.init(this);

        dialog.getElement().addEventListener("opened-changed", e->{
            if (!dialog.isOpened()){
                presenter.cancel();
            }
        });

       // grid.addColumn()
    }

    private void setupGrid() {
        grid.addColumn(Price::getId).setHeader("№").setWidth("50px").setFlexGrow(10);
        grid.addColumn(Price::getDate).setHeader("Дата").setFlexGrow(10);
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
    public void setConfirmDialog(ConfirmationDialog<Price> confirmDialog) {
        this.confirmation = confirmDialog;
    }

    @Override
    public ConfirmationDialog getConfirmDialog() {
        return confirmation;
    }

    public void setDialogElementsVisibility(boolean editing) {
        dialog.add(priceEditor);
        priceEditor.setVisible(editing);
    }
}
