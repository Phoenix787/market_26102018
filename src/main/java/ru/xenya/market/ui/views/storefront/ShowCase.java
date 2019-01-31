package ru.xenya.market.ui.views.storefront;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import ru.xenya.market.app.HasLogger;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.ui.MainView;
import ru.xenya.market.ui.components.SearchBar;
import ru.xenya.market.ui.components.common.ConfirmDialog;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.utils.converters.*;
import ru.xenya.market.ui.views.EntityView;
import ru.xenya.market.ui.views.orderedit.OrderEditor;
import ru.xenya.market.ui.views.orderedit.OrderPresenter;

import java.util.stream.Stream;

/**
 * A Designer generated component for the show-case.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("show-case")
@HtmlImport("src/views/storefront/show-case.html")
@Route(value = MarketConst.PAGE_SHOWCASE, layout = MainView.class)
@RouteAlias(value = MarketConst.PAGE_SHOWCASE_EDIT, layout = MainView.class)
@PageTitle(MarketConst.TITLE_SHOWCASE)
public class ShowCase extends PolymerTemplate<TemplateModel>
        implements HasLogger, HasUrlParameter<Long>, EntityView<Order> {

    @Id("search")
    private SearchBar searchBar;

    @Id("grid")
    private Grid<Order> grid;

    @Id("dialog")
    private Dialog dialog;

    private final OrderEditor editor;

    private final ShowOrderPresenter presenter;

    private ConfirmDialog confirmation;



    public ShowCase(OrderEditor editor, ShowOrderPresenter presenter) {
        this.editor = editor;
        this.presenter = presenter;
        this.confirmation = new ConfirmDialog();

       // searchBar.setActionText("");
        searchBar.setCheckboxText("Показать прошлые заказы");
        searchBar.setPlaceHolder("Поиск");
        searchBar.setActionButtonVisible(false);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addColumn(OrderCard.getTemplate()
                .withProperty("orderCard", OrderCard::create)
                .withProperty("header", order -> presenter.getHeaderByOrderId(order.getId()))
                .withEventHandler("cardClick",
                 order -> presenter.onNavigation(order.getId(), true)  /*presenter.load(order.getId())*/)) /*UI.getCurrent().navigate(MarketConst.PAGE_SHOWCASE + "/" + order.getId())))*/;

        getSearchBar().addFilterChangeListener(e-> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));

        presenter.init(this);

        dialog.getElement().addEventListener("opened-changed", e->{
            if (dialog.isOpened()) {
               // presenter.cancel();
            }
        });
    }

    public void setOpened(boolean isOpened) {
        dialog.setOpened(isOpened);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long orderId) {

        boolean editView = event.getLocation().getPath().contains(MarketConst.PAGE_SHOWCASE_EDIT);
        if (orderId != null) {
            presenter.onNavigation(orderId, true);
        } else if (dialog.isOpened()) {
            presenter.closeSilently();
        }
    }

    void navigateToMainView(){
        getUI().ifPresent(ui->ui.navigate(MarketConst.PAGE_SHOWCASE));
    }

    @Override
    public boolean isDirty() {
        return editor.hasChanges();
    }

    @Override
    public void clear() {
        editor.clear();
    }

    @Override
    public void write(Order entity) throws ValidationException {
        getOpenedOrderEditor().write(entity);
    }

    @Override
    public String getEntityName() {
        return EntityUtil.getName(Order.class);
    }

    @Override
    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmation = confirmDialog;
    }

    @Override
    public ConfirmDialog getConfirmDialog() {
        return confirmation;
    }

    public Grid<Order> getGrid() {
        return grid;
    }


    public SearchBar getSearchBar() {
        return searchBar;
    }

    OrderEditor getOpenedOrderEditor() {
        return editor;
    }

    public void setDialogElementsVisibility(boolean editing) {
        dialog.add(editor);
        editor.setVisible(editing);
    }

    void openDialog(){
        dialog.setOpened(true);
    }

    void closeDialog() {
        dialog.setOpened(false);
    }

    public Stream<HasValue<?,?>> validate(){
        return getOpenedOrderEditor().validate();
    }

    void updateTitle(boolean newEntity) {
        getOpenedOrderEditor().getTitle().setText((newEntity ? "Новый" : "Редактировать") + " заказ");
    }
    /**
     * This model binds properties between ShowCase and show-case.html
     */
    public interface ShowCaseModel extends TemplateModel {
        // Add setters and getters for template properties here.

        @Include({"id", "dueDate", "orderState", /*"pricePlan.date", */"items.price.name", "items.quantity",
                "items.totalPrice"})
        @Encode(value = LongToStringEncoder.class, path = "id")
        @Encode(value = LocalDateToStringEncoder.class, path = "dueDate")
        @Encode(value = OrderStateConverter.class, path = "orderState")
        @Encode(value = CurrencyFormatter.class, path = "items.totalPrice")
        void setItem(Order order);
    }
}
