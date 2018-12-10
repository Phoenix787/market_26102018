package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xenya.market.backend.data.entity.OrderItem;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
