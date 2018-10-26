package ru.xenya.market.backend.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

//счета
//@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue
    @Column(name = "invoice_id")
    private Long invoiceId;

    @NotBlank
    private String numberInvoice;

    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDate dateInvoice;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "invoice")
//    private Order order;

    @java.beans.ConstructorProperties({"numberInvoice", "dateInvoice"})
    public Invoice(@NotBlank String numberInvoice, LocalDate dateInvoice) {
        this.numberInvoice = numberInvoice;
        this.dateInvoice = dateInvoice;
    }

    public Invoice() {
    }

    public Invoice(String numberInvoice) {
        this.numberInvoice = numberInvoice;
        this.dateInvoice = LocalDate.now();
    }
}
