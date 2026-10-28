package com.examly.springapp.repository;

import com.examly.springapp.entity.Holiday;
import com.examly.springapp.entity.Holiday.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByAcademicYear(int academicYear);

    List<Holiday> findByDateBetween(LocalDate from, LocalDate to);

    List<Holiday> findByHolidayType(HolidayType holidayType);

    Optional<Holiday> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);
}
