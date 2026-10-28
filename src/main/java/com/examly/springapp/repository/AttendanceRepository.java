package com.examly.springapp.repository;

import com.examly.springapp.entity.Attendance;
import com.examly.springapp.entity.Attendance.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate from, LocalDate to);

    List<Attendance> findByDate(LocalDate date);

    boolean existsByStudentIdAndDate(Long studentId, LocalDate date);

    long countByStudentIdAndStatus(Long studentId, Status status);

    @Query("SELECT a.student.id FROM Attendance a GROUP BY a.student.id " +
           "HAVING (SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) * 100.0 / COUNT(a)) < 75")
    List<Long> findStudentIdsBelow75Percent();

    // FR9: Chronic absenteeism — students with absences above threshold
    @Query("SELECT a.student.id FROM Attendance a WHERE a.status = 'ABSENT' " +
           "GROUP BY a.student.id HAVING COUNT(a) >= :threshold")
    List<Long> findStudentIdsWithAbsencesAboveThreshold(@Param("threshold") int threshold);

    // FR8: Annual attendance summary — present/absent counts per student
    @Query("SELECT new map(a.student.id as studentId, " +
           "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) as presentCount, " +
           "SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) as absentCount, " +
           "COUNT(a) as totalDays) " +
           "FROM Attendance a GROUP BY a.student.id")
    List<Map<String, Object>> findAnnualAttendanceSummary();
}
