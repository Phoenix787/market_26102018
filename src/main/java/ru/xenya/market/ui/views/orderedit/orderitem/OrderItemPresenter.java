//package ru.xenya.market.ui.views.orderedit.orderitem;
//
//import com.vaadin.flow.spring.annotation.SpringComponent;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Scope;
//import ru.xenya.market.backend.data.entity.Order;
//import ru.xenya.market.backend.data.entity.Price;
//import ru.xenya.market.backend.data.entity.ScheduleDates;
//import ru.xenya.market.backend.data.entity.User;
//import ru.xenya.market.backend.service.PriceService;
//import ru.xenya.market.ui.views.admin.settings.ScheduleDatePresenter;
//
//import java.util.List;
//import java.util.Set;
//
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//public class OrderItemPresenter {
//
//    private OrderItemsView view;
//
//    private final PriceService priceService;
//    private final ScheduleDatePresenter datePresenter;
//    private final User currentUser;
//    private Order currentOrder;
//
//
//
//    public OrderItemPresenter(PriceService priceService, ScheduleDatePresenter datePresenter, User currentUser) {
//        this.priceService = priceService;
//        this.datePresenter = datePresenter;
//        this.currentUser = currentUser;
//    }
//
//    public void setView(OrderItemsView view) {
//        this.view = view;
//
//    }
//
//    public Price getDefaultPrice(){
//        return priceService.findPriceByDefault(true);
//    }
//
//    public List<ScheduleDates> getDates() {
//        return datePresenter.updateList();
//    }
//
//    public Set<ScheduleDates> getDatesAfterNow() {
//        return datePresenter.getDatesFromCurrent();
//    }
//}
