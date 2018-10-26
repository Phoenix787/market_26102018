package ru.xenya.market.ui.dataproviders;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.service.OrderService;

import java.io.Serializable;
import java.util.List;

public class OrdersGridDataProvider extends FilterablePageableDataProvider<Order,OrdersGridDataProvider.OrderFilter> {


    private final OrderService orderService;

    public OrdersGridDataProvider(OrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    protected Page<Order> fetchFromBackEnd(Query<Order, OrderFilter> query, Pageable pageable) {
        return null;
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return null;
    }

    @Override
    protected int sizeInBackEnd(Query<Order, OrderFilter> query) {
        return 0;
    }


    public static class OrderFilter implements Serializable {
        private String filter;
        private boolean showPrevious;

        public String getFilter() {
            return filter;
        }

        public boolean isShowPrevious() {
            return showPrevious;
        }

        public OrderFilter(String filter, boolean showPrevious) {
            this.filter = filter;
            this.showPrevious = showPrevious;
        }

        public static OrderFilter getEmptyFilter() {
            return new OrderFilter("", false);
        }
    }
}
