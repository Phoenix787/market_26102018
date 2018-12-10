package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.OrderHistoryItem;

public interface OrderHistoryItemRepository extends JpaRepository<OrderHistoryItem, Long> {
}
