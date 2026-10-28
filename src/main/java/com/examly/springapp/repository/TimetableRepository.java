package com.examly.springapp.repository;

import com.examly.springapp.entity.Timetable;
import com.examly.springapp.entity.Timetable.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByClassSectionAndActiveTrue(String classSection);

    List<Timetable> findByTeacherIdAndActiveTrue(Long teacherId);

    Optional<Timetable> findByTeacherIdAndDayAndPeriodAndActiveTrue(Long teacherId, Day day, int period);

    boolean existsByTeacherIdAndDayAndPeriodAndActiveTrue(Long teacherId, Day day, int period);

    List<Timetable> findByTeacherIdAndDayAndActiveTrue(Long teacherId, Day day);

    List<Timetable> findByDayAndPeriodAndActiveTrue(Day day, int period);

    @Query("SELECT t FROM Timetable t WHERE t.active = true AND EXISTS (" +
           "SELECT t2 FROM Timetable t2 WHERE t2.active = true " +
           "AND t2.teacher.id = t.teacher.id AND t2.day = t.day " +
           "AND t2.period = t.period AND t2.id <> t.id)")
    List<Timetable> findAllClashes();

    // FR9: Teacher workload — period count per teacher
    @Query("SELECT new map(t.teacher.id as teacherId, COUNT(t) as periodCount) " +
           "FROM Timetable t WHERE t.active = true GROUP BY t.teacher.id")
    List<Map<String, Object>> findTeacherPeriodCount();
}
