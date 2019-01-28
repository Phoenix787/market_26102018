package ru.xenya.market.backend.data.entity.util;

import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.backend.data.entity.PriceItem;

public interface OrderItemSummary {
    Long getId();
    String getDatesString();
    PriceItem getPrice();
    Unit getUnit();
    Integer getTotalPrice();
}
