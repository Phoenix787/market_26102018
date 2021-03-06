package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import ru.xenya.market.backend.data.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByFullNameIgnoreCase(String fullName);
    @EntityGraph(value = Customer.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Customer> findBy(Pageable pageable);

    List<Customer> findByFullNameContainingIgnoreCase(String fullNameLike);

    List<Customer> findByFullNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String fullNameLike, String addressLike);

    @EntityGraph(value = Customer.ENTITY_GRAPH_BRIEF, type = EntityGraph.EntityGraphType.LOAD)
    Page<Customer> findByFullNameLikeIgnoreCaseOrAddressLikeIgnoreCase(
            String fullNameLike, String addressLike, Pageable pageable);

    long countByFullNameLikeIgnoreCaseOrAddressLikeIgnoreCaseOrPhoneNumberForSMSIgnoreCaseOrPhoneNumbersLikeIgnoreCase(
            String fullNameLike, String addressLike, String phoneNumberForSMSLike, String phoneNumbersLike);

}
