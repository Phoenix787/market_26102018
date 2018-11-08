package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

//счета
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Invoice extends AbstractEntity {

//    @NotBlank
    private String numberInvoice;

//    @NotNull(message = "{market.due.dueDate.required}")
    private LocalDate dateInvoice;

//    @OneToOne (cascade = CascadeType.ALL, mappedBy = "invoice")
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

    public String getNumberInvoice() {
        return numberInvoice;
    }

    public void setNumberInvoice(String numberInvoice) {
        this.numberInvoice = numberInvoice;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }
}
