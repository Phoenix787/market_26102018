package ru.xenya.market.backend.data.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.List;

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

    @Override
    public String toString() {
        return new LocalDateToStringEncoder().encode(date);
    }
}
