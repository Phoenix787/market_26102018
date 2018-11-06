package ru.xenya.market.ui.views.admin.prices;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.service.FilterableCrudService;
import ru.xenya.market.backend.service.PriceService;
import ru.xenya.market.ui.crud.CrudEntityPresenter;
import ru.xenya.market.ui.crud.EntityPresenter;
import ru.xenya.market.ui.views.admin.prices.PricesView;

import java.util.List;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PricePresenter {

    private PricesView view;

    private final EntityPresenter<Price, PricesView> entityPresenter;

    private final User currentUser;

    private final PriceService priceService;

    private Price currentPrice;

    public PricePresenter(EntityPresenter<Price, PricesView> entityPresenter,
                          User currentUser, PriceService priceService) {
        this.priceService = priceService;
        this.entityPresenter = entityPresenter;
        this.currentUser = currentUser;

    }

    public void init(PricesView view) {
        this.entityPresenter.setView(view);
        this.view = view;
        System.err.println(this.entityPresenter.getView().getClass().getName());
        view.getGrid().setItems(updateList());
        view.getForm().setCurrentPrice(currentPrice);
        view.getForm().setCurrentUser(currentUser);
        view.getForm().addCancelListener(e -> cancel());
        view.getForm().addSaveListener(e -> save());
        view.getForm().addDeleteListener(e->delete());
    }


    public void onNavigation(Long id, boolean edit){
        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    public void createNewPrice() {
        open(entityPresenter.createNew(), true);
    }

    public void cancel(){
        entityPresenter.cancel(()->close(), ()->view.setOpened(true));
    }


    public void closeSilently() {
        entityPresenter.close();
        view.getConfirmDialog().setOpened(false);
        view.setOpened(false);
    }

    public void save() {
        entityPresenter.save(e->{
            if (entityPresenter.isNew()) {
                List<Price> prices = priceService.findPricesByDefault(true);
                for (Price price : prices) {
                    price.changeDefault(currentUser, false);
                    priceService.save(currentUser, price);
                }
                view.showCreatedNotification("Прайс");
                view.getGrid().setItems(updateList());
            } else {
                addComment("Прайс изменён");
                view.showUpdateNotification("Прайс # " + e.getId());
                view.getGrid().setItems(updateList());
            }
            close();
        });
    }

    public void close(){
        view.setOpened(false);
        view.getOpenedEditor().close();
        view.navigateToMainView();
        entityPresenter.close();
    }


    public void delete(){
        entityPresenter.delete(e->{
            view.showDeleteNotification("Прайс #" + e.getId());
//            addComment("Прайс удалён");       //может добавить какую-то таблицу-сущность куда сохранять все изменения сделанные конкретным пользователем
            view.getGrid().setItems(updateList());
            closeSilently();
        });
    }

    private void open(Price price, boolean edit) {
        view.setDialogElementsVisibility(edit);
        view.setOpened(true);
        if (edit) {
            view.getOpenedEditor().read(price, entityPresenter.isNew());
        }
    }

    public List<Price> updateList() {
        return priceService.findAll();
    }

    public List<Price> updateList(String filter) {
        if (filter != null && !filter.isEmpty()){
            return priceService.findPricesByDate(filter);
        } else {
            return priceService.findAll();
        }
    }

    void addComment(String comment) {
        if (entityPresenter.executeUpdate(e -> priceService.addComment(currentUser, e, comment))) {
            // You can only add comments when in view mode, so reopening in that state.
            open(entityPresenter.getEntity(), false);
        }
    }

    public void filter(String filter) {
        if (filter != null && !filter.isEmpty()) {
            view.getGrid().setItems(updateList(filter));
        } else {
            view.getGrid().setItems(updateList());
        }
    }
}
