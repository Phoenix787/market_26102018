package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.Price;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findByDate(LocalDate date);

    @EntityGraph(value = Price.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Price findByDefaultPrice(boolean isDefault);

    @EntityGraph(value = Price.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Price> findBy(Pageable pageable);

    @EntityGraph(value = Price.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Price> findByDate(LocalDate date, Pageable pageable);

    @Override
    @EntityGraph(value = Price.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    List<Price> findAll();

    @Override
    @EntityGraph(value = Price.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Price> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = Price.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Price> findById(Long id);


    Page<Price> findByDefaultPrice(boolean isDefault, Pageable pageable);

    long countByDefaultPrice(boolean isDefault);

    long countByDate(LocalDate date);
}
