package ru.xenya.market.backend.data.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class ScheduleDates extends AbstractEntity {

    LocalDate date;

    private String weekDay;

    public ScheduleDates() {
    }

    public ScheduleDates(LocalDate date, String weekDay) {
        this.date = date;
        this.weekDay = weekDay;
    }
}
