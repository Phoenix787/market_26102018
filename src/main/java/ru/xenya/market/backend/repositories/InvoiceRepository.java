package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


}
