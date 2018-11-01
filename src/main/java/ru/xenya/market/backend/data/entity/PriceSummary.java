package ru.xenya.market.backend.data.entity;

import java.time.LocalDate;
import java.util.List;

public interface PriceSummary {
    Long getId();

    Boolean isDefault();

    List<PriceItem> getItems();

    LocalDate getDate();
}
