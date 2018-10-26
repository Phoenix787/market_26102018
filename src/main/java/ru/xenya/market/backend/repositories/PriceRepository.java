package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.Price;

import java.time.LocalDate;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByDate(LocalDate date);

    List<Price> findByDefaultPrice(boolean isDefault);

    long countByDefaultPrice(boolean isDefault);
}
