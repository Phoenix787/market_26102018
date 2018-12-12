package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.entity.Customer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.OrderSummary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

//@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order, Long>/*, OrderRepositoryCustom*/ {
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Order> findByDueDateAfter(LocalDate filterDate, Pageable pageable);

    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Order> findByCustomerFullNameContainingIgnoreCase(String searchQuery, Pageable pageable);

    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Order> findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate, Pageable pageable);

    @Override
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAll();

    @Override
    @EntityGraph(value = Order.ENTITY_GRAPTH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Order> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = Order.ENTITY_GRAPTH_FULL, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findById(Long id);

    List<Order> findByCustomer(Customer customer);

    long countByDueDateAfter(LocalDate date);

    long countByCustomerFullNameContainingIgnoreCase(String searchQuery);

    List<Order> findByCustomerAndDueDate(Customer customer, LocalDate dueDateFilter);

    long countByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(String s, LocalDate date);



}
