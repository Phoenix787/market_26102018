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

@Service
public class ScheduleDatesService{

    private ScheduleDatesRepository repository;

//    private Map<Integer, String> weekDays = Collections.unmodifiableMap(new HashMap<Integer, String>(){{
//        put(1, "Понедельник"); put(2, "Вторник"); put(3, "Среда");
//        put(4, "Четверг"); put(5, "Пятница"); put(6, "Суббота"); put(7, "Воскресенье");
//    }});


    @Autowired
    public ScheduleDatesService(ScheduleDatesRepository repository) {
        this.repository = repository;
    }



    public void createNew(LocalDate start, LocalDate end) {
      //  long daysBetween = DAYS.between(start, end);
        Period period = Period.between(start, end);

        LocalDate current = start;
        for (int i = 0; i < period.getDays(); i++) {
            if (current.getDayOfWeek() == DayOfWeek.TUESDAY ||
                    current.getDayOfWeek() == DayOfWeek.THURSDAY ||
                    current.getDayOfWeek() == DayOfWeek.SATURDAY) {
                //String nameDay = weekDays.get(start.getDayOfWeek().getValue());
                repository.save(new ScheduleDates(current,
                        current.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"))));
               current = current.plusDays(i++);

            }
        }

    }



}
