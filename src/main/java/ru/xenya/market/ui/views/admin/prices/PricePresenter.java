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
public class PricePresenter extends CrudEntityPresenter<Price> {

    private PricesView view;

    private final EntityPresenter<Price, PricesView> entityPresenter;

    private User currentUser;

    private final PriceService priceService;

    private Price currentPrice;

    public PricePresenter(EntityPresenter<Price, PricesView> entityPresenter,
                          User currentUser, PriceService priceService) {
        super(priceService, currentUser);
        this.entityPresenter = entityPresenter;
        this.currentUser = currentUser;
        this.priceService = priceService;
    }

    public void init(PricesView view) {
        this.view = view;
        this.entityPresenter.setView(view);
        this.view.getGrid().setItems(updateList());
        this.view.getOpenedEditor().setCurrentPrice(currentPrice);
        this.view.getOpenedEditor().addCancelListener(e -> cancel());
        this.view.getOpenedEditor().addSaveListener(e -> save());
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

    @Override
    public void closeSilently() {
        entityPresenter.close();
        view.setOpened(false);
    }

    public void save() {
        entityPresenter.save(e->{
            if (entityPresenter.isNew()) {
                view.showCreatedNotification();
                view.getGrid().setItems(updateList());
            } else {
                view.showUpdateNotification();
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
}
