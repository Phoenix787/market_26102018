package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xenya.market.backend.data.entity.HistoryItem;

@Repository
public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
