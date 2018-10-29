package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.Price;

import java.time.LocalDate;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByDate(LocalDate date);

    List<Price> findByDefaultPrice(boolean isDefault);

    Page<Price> findBy(Pageable pageable);

    Page<Price> findByDate(LocalDate date, Pageable pageable);

    Page<Price> findByDefaultPrice(boolean isDefault, Pageable pageable);

    long countByDefaultPrice(boolean isDefault);

    long countByDate(LocalDate date);
}
