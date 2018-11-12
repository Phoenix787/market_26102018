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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
}
