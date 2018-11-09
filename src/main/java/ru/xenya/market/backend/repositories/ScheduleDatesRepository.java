package ru.xenya.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xenya.market.backend.data.entity.ScheduleDates;

public interface ScheduleDatesRepository extends JpaRepository<ScheduleDates, Long> {
}
