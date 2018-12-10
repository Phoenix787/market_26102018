package ru.xenya.market.ui.crud;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import ru.xenya.market.backend.data.entity.AbstractEntity;
import ru.xenya.market.backend.service.CrudService;
import ru.xenya.market.backend.service.FilterableCrudService;

import java.util.List;
public class CrudEntityDataProvider<T extends AbstractEntity> extends FilterablePageableDataProvider<T, String> {

    private final FilterableCrudService<T> crudService;
    private List<QuerySortOrder> defaultSortOrders;

    public CrudEntityDataProvider(FilterableCrudService<T> crudService) {
        this.crudService = crudService;
        setSortOrders();
    }

    private void setSortOrders(){
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        builder.thenAsc("id");
        defaultSortOrders = builder.build();
    }

    @Override
  //  @Transactional(isolation = Isolation.SERIALIZABLE)
    protected Page<T> fetchFromBackEnd(Query<T, String> query, Pageable pageable) {
        return crudService.findAnyMatching(query.getFilter(), pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<T, String> query) {
        return (int) crudService.countAnyMatching(query.getFilter());
    }
}
