package ru.xenya.market.ui.dataproviders;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.data.domain.PageRequest;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;
import ru.xenya.market.backend.service.PriceService;

import java.util.stream.Stream;


@SpringComponent
public class PriceDataProvider extends AbstractBackEndDataProvider<Price, String> {

    private final PriceService priceService;

    public PriceDataProvider(PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    protected Stream<Price> fetchFromBackEnd(Query<Price, String> query) {
        Stream<Price> stream = priceService.findAll().stream();
        return stream.skip(query.getOffset()).limit(query.getLimit());

//        return priceService.findAnyMatching(query.getFilter(), PageRequest.of(query.getOffset(), query.getLimit())).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Price, String> query) {
        return (int) priceService.countAnyMatching(query.getFilter());
    }




}
