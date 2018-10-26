package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByFullNameIgnoreCase(String fullName);

    Page<Customer> findBy(Pageable pageable);

    List<Customer> findByFullNameContainingIgnoreCase(String fullNameLike);

    List<Customer> findByFullNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String fullNameLike, String addressLike);

    Page<Customer> findByFullNameLikeIgnoreCaseOrAddressLikeIgnoreCase(
            String fullNameLike, String addressLike, Pageable pageable);

    long countByFullNameLikeIgnoreCaseOrAddressLikeIgnoreCaseOrPhoneNumberForSMSIgnoreCaseOrPhoneNumbersLikeIgnoreCase(
            String fullNameLike, String addressLike, String phoneNumberForSMSLike, String phoneNumbersLike);

}
