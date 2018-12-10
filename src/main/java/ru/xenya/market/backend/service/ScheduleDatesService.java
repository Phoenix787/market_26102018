package ru.xenya.market.backend.service;


import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static ru.xenya.market.ui.utils.MarketConst.APP_LOCALE;

@Service
@org.springframework.transaction.annotation.Transactional(isolation = Isolation.SERIALIZABLE)
public class ScheduleDatesService /*implements FilterableCrudService<ScheduleDates>*/{

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

    public List<ScheduleDates> findDatesAfterCurrent(LocalDate now) {
        return repository.findByDateAfter(now);
    }

    public Page<ScheduleDates> findAnyMatchingAfterDate(Optional<LocalDate> optionalFilterDate, Pageable pageable) {
        if (optionalFilterDate.isPresent()){
            return repository.findByDateAfter(optionalFilterDate.get(), pageable);
        } else {
            return repository.findBy(pageable);
        }
    }

    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return repository.countByDate(repositoryFilter);
        } else {
            return count();
        }
    }

    public long countAnyMatchingAfterDate(Optional<LocalDate> optionalFilterDate){
        if (optionalFilterDate.isPresent()) {
            return repository.countByDate(optionalFilterDate.get());
        } else {
            return repository.count();
        }
    }

//    @Override
    public Page<ScheduleDates> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return repository.findByDate(repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }



    public Page<ScheduleDates> find(Pageable pageable) {
        return repository.findBy(pageable);
    }


//    @Override
//    public ScheduleDatesRepository getRepository() {
//        return repository;
//    }
//
//    @Override
//    public ScheduleDates createNew(User user) {
//        return new ScheduleDates();
//    }

    public List<ScheduleDates> findAll(int offset, int limit, Map<String, Boolean> sortOrders) {
        int page = offset / limit;
        List<Sort.Order> orders = sortOrders.entrySet().stream()
                .map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC, e.getKey()))
                .collect(Collectors.toList());

        PageRequest pageRequest = new PageRequest(page, limit, orders.isEmpty() ? null : new Sort(orders));
        List<ScheduleDates> items = repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    public Integer count() {
        return Math.toIntExact(repository.count());
    }

}
