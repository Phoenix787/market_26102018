package ru.xenya.market.ui.dataproviders;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.data.domain.PageRequest;
import ru.xenya.market.backend.data.entity.PriceItem;

import java.util.stream.Stream;


//todo PriceItemService, ItemPriceRepository
@SpringComponent
public class PriceDataProvider extends AbstractBackEndDataProvider<PriceItem, String> {

    private final PriceItemService itemPriceService;

    public PriceDataProvider(PriceItemService itemPriceService) {
        this.itemPriceService = itemPriceService;
    }

    @Override
    protected Stream<PriceItem> fetchFromBackEnd(Query<PriceItem, String> query) {
        return itemPriceService.findAnyMatching(query.getFilter(), PageRequest.of(query.getOffset(), query.getLimit()))
                .stream();
    }

    @Override
    protected int sizeInBackEnd(Query<PriceItem, String> query) {
        return (int) itemPriceService.countAnyMatching(query.getFilter());
    }
}
