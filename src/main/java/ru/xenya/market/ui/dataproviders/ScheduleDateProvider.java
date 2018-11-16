package ru.xenya.market.ui.dataproviders;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.service.ScheduleDatesService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SpringComponent
public class ScheduleDateProvider extends AbstractBackEndDataProvider<ScheduleDates, String> {

    private final ScheduleDatesService datesService;
    private Consumer<Page<ScheduleDates>> pageObserver;

    public ScheduleDateProvider(ScheduleDatesService datesService) {
        this.datesService = datesService;
    }


    @Override
    protected Stream<ScheduleDates> fetchFromBackEnd(Query<ScheduleDates, String> query) {
        return datesService.findAnyMatching(query.getFilter(), PageRequest.of(query.getOffset(), query.getLimit())).stream();
//        Page<ScheduleDates> page = datesService.findAnyMatchingAfterDate(getFilterDate(), PageRequest.of(query.getOffset(), query.getOffset() + query.getLimit()));
//        return page.stream();
    }

//    @Override
//    protected Page<ScheduleDates> fetchFromBackEnd(Query<ScheduleDates, String> query, Pageable pageable) {
//        return datesService.findAnyMatchingAfterDate(getFilterDate(), PageRequest.of(query.getOffset(), query.getLimit()));
//    }
//
//    @Override
//    protected List<QuerySortOrder> getDefaultSortOrders() {
//        return null;
//    }

    @Override
    protected int sizeInBackEnd(Query<ScheduleDates, String> query) {
        return (int) datesService.countAnyMatching(query.getFilter());
    }


    private Optional<LocalDate> getFilterDate() {
         return Optional.of(LocalDate.now().minusDays(1));
    }
}
