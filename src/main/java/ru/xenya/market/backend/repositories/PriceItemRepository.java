package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.Price;
import ru.xenya.market.backend.data.entity.PriceItem;

import java.util.List;

public interface PriceItemRepository extends JpaRepository<PriceItem, Long> {

    Page<PriceItem> findBy(Pageable pageable);

    Page<PriceItem> findByNameLikeIgnoreCase(String name, Pageable page);

    int countByNameLikeIgnoreCase(String name);

    List<PriceItem> findByOwner(Price price);
}
