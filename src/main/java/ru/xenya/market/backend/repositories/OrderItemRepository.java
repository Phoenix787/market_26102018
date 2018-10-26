package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
