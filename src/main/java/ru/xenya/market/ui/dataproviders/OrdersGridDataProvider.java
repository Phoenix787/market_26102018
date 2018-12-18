package ru.xenya.market.ui.dataproviders;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.service.OrderService;
import ru.xenya.market.ui.utils.MarketConst;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrdersGridDataProvider
        extends FilterablePageableDataProvider<Order,OrdersGridDataProvider.OrderFilter> {

    public static class OrderFilter implements Serializable {
       private String filter;
        private Long filterId;
        private boolean showPrevious;

        public String getFilter() {
            return filter;
        }
        public Long getFilterId(){ return filterId;}
//        public String getFilter() {
//            return filter;
//        }

        public boolean isShowPrevious() {
            return showPrevious;
        }

        public OrderFilter(String filter, Long filterId, boolean showPrevious) {
            this.filter = filter;
            this.filterId = filterId;
            this.showPrevious = showPrevious;
        }

        public OrderFilter(String filter, boolean showPrevious) {
            this.filter = filter;
            this.filterId = 0L;
            this.showPrevious = showPrevious;
        }
//public OrderFilter(String filter, boolean showPrevious) {
//            this.filter = filter;
//            this.showPrevious = showPrevious;
//        }

//        public static OrderFilter getEmptyFilter() {
//            return new OrderFilter("", false);
//        }
        public static OrderFilter getEmptyFilter() {
            return new OrderFilter("", null,  false);
        }
    }



    private final OrderService orderService;
    private List<QuerySortOrder> defaultSortOrders;
    private Consumer<Page<Order>> pageObserver;

    public OrdersGridDataProvider(OrderService orderService) {

        this.orderService = orderService;
        setSortOrders(MarketConst.DEFAULT_SORT_DIRECTION, MarketConst.ORDER_SORT_FIELDS);

    }

    private void setSortOrders(Sort.Direction direction, String[] properties) {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        for (String property : properties) {
            if (direction.isAscending()) {
                builder.thenAsc(property);
            } else {
                builder.thenDesc(property);
            }
        }
        defaultSortOrders = builder.build();
    }


    @Override
    protected Page<Order> fetchFromBackEnd(Query<Order, OrderFilter> query, Pageable pageable) {
        OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
//        Page<Order> page = orderService.findAnyMatchingAfterDueDate(Optional.ofNullable(filter.getFilter()),
//                getFilterDate(filter.isShowPrevious()), pageable);
        Page<Order> page = orderService.findAnyMatching(Optional.of(filter.getFilterId()), Optional.ofNullable(filter.getFilter()), pageable);
        if (pageObserver != null) {
            pageObserver.accept(page);
        }

        return page;
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<Order, OrderFilter> query) {

        OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
//        return (int)orderService.countAnyMatchingAfterDueDate(Optional.ofNullable(filter.getFilter()),
//                getFilterDate(filter.isShowPrevious()));
//        return (int)orderService.countAnyMatching(Optional.ofNullable(filter.getFilter()), Optional.ofNullable(filter.getFilterId()));
        return (int)orderService.countAnyMatching(Optional.ofNullable(filter.getFilterId()));
    }

    private Optional<LocalDate> getFilterDate(boolean showPrevious) {
        if (showPrevious) {
            return Optional.empty();
        }
        return Optional.of(LocalDate.now().minusDays(1));
    }

    public void setPageObserver(Consumer<Page<Order>> pageObserver){
        this.pageObserver = pageObserver;
    }


}
