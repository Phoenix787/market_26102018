package ru.xenya.market.backend.data.entity;
//платежи

import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Repayment extends AbstractEntity {

    @NotNull
    private LocalDate date;
    @NotNull
    private int sum;
    @NotNull
    private Payment payment;

    public Repayment() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
