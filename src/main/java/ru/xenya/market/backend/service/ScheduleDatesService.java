package ru.xenya.market.backend.service;


import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.repositories.ScheduleDatesRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static ru.xenya.market.ui.utils.MarketConst.APP_LOCALE;

@Service
public class ScheduleDatesService{

    private ScheduleDatesRepository repository;

    @Autowired
    public ScheduleDatesService(ScheduleDatesRepository repository) {
        this.repository = repository;
    }


    public void createNew(LocalDate start, LocalDate end) {

        while (!start.isAfter(end)) {
            System.out.println(start);
            if (start.getDayOfWeek() == DayOfWeek.TUESDAY ||
                    start.getDayOfWeek() == DayOfWeek.THURSDAY ||
                    start.getDayOfWeek() == DayOfWeek.SATURDAY) {
                repository.save(new ScheduleDates(start, start.getDayOfWeek().getDisplayName(TextStyle.FULL, APP_LOCALE)));

            }
            start = start.plusDays(1);


        }
    }


    public List<ScheduleDates> findAll() {
        return repository.findAll();
    }
}
