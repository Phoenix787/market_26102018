package ru.xenya.market.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.ScheduleDates;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleDatesRepository extends JpaRepository<ScheduleDates, Long> {

    List<ScheduleDates> findByDateAfter(LocalDate filterDate);

    Page<ScheduleDates> findByDateAfter(LocalDate filterDate, Pageable page);

    int countByDate(String repositoryFilter);
    int countByDate(LocalDate repositoryFilter);

    Page<ScheduleDates> findByDate(String filter, Pageable page);

    Page<ScheduleDates> findBy(Pageable page);
}
