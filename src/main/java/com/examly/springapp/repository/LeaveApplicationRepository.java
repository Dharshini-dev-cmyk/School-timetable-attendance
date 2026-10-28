package com.examly.springapp.repository;

import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.entity.LeaveApplication.LeaveStatus;
import com.examly.springapp.entity.LeaveApplication.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findByStatus(LeaveStatus status);

    List<LeaveApplication> findByTeacherId(Long teacherId);

    long countByTeacherIdAndLeaveTypeAndStatus(Long teacherId, LeaveType leaveType, LeaveStatus status);

    // FR9: Leave count per teacher
    @Query("SELECT new map(l.teacher.id as teacherId, COUNT(l) as leaveCount) " +
           "FROM LeaveApplication l WHERE l.status = :status GROUP BY l.teacher.id")
    List<Map<String, Object>> findLeaveCountByTeacher(@Param("status") LeaveStatus status);

    // FR9: Substitute burden report
    @Query("SELECT new map(l.substituteTeacher.id as substituteId, COUNT(l) as assignedCount) " +
           "FROM LeaveApplication l WHERE l.status = :status AND l.substituteTeacher IS NOT NULL " +
           "GROUP BY l.substituteTeacher.id ORDER BY COUNT(l) DESC")
    List<Map<String, Object>> findSubstituteBurdenReport(@Param("status") LeaveStatus status);
}
