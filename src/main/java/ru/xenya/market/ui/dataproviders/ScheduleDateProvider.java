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
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SpringComponent
public class ScheduleDateProvider extends AbstractBackEndDataProvider<ScheduleDates, ScheduleDateProvider.ScheduleDatesFilter> {

    private LocalDateToStringEncoder encoder = new LocalDateToStringEncoder();

    public static class ScheduleDatesFilter implements Serializable {
        private static String filter;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public ScheduleDatesFilter(String filter) {
            this.filter = filter;
        }

        public static ScheduleDatesFilter getEmptyFilter() {
            return new ScheduleDatesFilter("");
        }
    }

    private final ScheduleDatesService datesService;
    private Consumer<Page<ScheduleDates>> pageObserver;

    public ScheduleDateProvider(ScheduleDatesService datesService) {
        this.datesService = datesService;
    }


    @Override
    protected Stream<ScheduleDates> fetchFromBackEnd(Query<ScheduleDates, ScheduleDatesFilter> query) {
        ScheduleDatesFilter filter = query.getFilter().orElse(ScheduleDatesFilter.getEmptyFilter());
        Page<ScheduleDates> page = datesService.findAnyMatchingAfterDate(
                Optional.of(encoder.decode(filter.getFilter())), PageRequest.of(query.getOffset(), query.getLimit()));
//        return datesService.findAnyMatching(query.getFilter(), PageRequest.of(query.getOffset(), query.getLimit())).stream();
//        Page<ScheduleDates> page = datesService.findAnyMatchingAfterDate(getFilterDate(), PageRequest.of(query.getOffset(), query.getOffset() + query.getLimit()));
        if (pageObserver != null) {
            pageObserver.accept(page);
        }
        return page.stream();
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
    protected int sizeInBackEnd(Query<ScheduleDates, ScheduleDatesFilter> query) {
        ScheduleDatesFilter filter = query.getFilter().orElse(ScheduleDatesFilter.getEmptyFilter());
        return (int) datesService.countAnyMatchingAfterDate(Optional.ofNullable(encoder.decode(filter.getFilter())));
//        return (int) datesService.countAnyMatching(query.getFilter());
    }


    private Optional<LocalDate> getFilterDate() {
         return Optional.of(LocalDate.now().minusDays(1));
    }

    public void setPageObserver(Consumer<Page<ScheduleDates>> pageObserver) {
        this.pageObserver = pageObserver;
    }

    public void setFilter(String filter) {
        ScheduleDatesFilter.filter = filter;
    }
}
