package com.examly.springapp.repository;

import com.examly.springapp.entity.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {

    Optional<AcademicCalendar> findByAcademicYear(int academicYear);

    Optional<AcademicCalendar> findByActiveTrue();

    boolean existsByAcademicYear(int academicYear);
}
