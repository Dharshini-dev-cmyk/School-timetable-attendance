package com.examly.springapp.service;

import com.examly.springapp.repository.AttendanceRepository;
import com.examly.springapp.repository.LeaveApplicationRepository;
import com.examly.springapp.repository.TimetableRepository;
import com.examly.springapp.entity.LeaveApplication.LeaveStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveApplicationRepository leaveRepository;
    private final TimetableRepository timetableRepository;

    public AnalyticsService(AttendanceRepository attendanceRepository,
                            LeaveApplicationRepository leaveRepository,
                            TimetableRepository timetableRepository) {
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
        this.timetableRepository = timetableRepository;
    }

    // FR9: Students below 75% — early warning dashboard
    public List<Long> getAtRiskStudents() {
        return attendanceRepository.findStudentIdsBelow75Percent();
    }

    // FR9: Chronic absenteeism — students with 10+ absences
    public List<Long> getChronicAbsentees() {
        return attendanceRepository.findStudentIdsWithAbsencesAboveThreshold(10);
    }

    // FR9: Teacher leave utilisation — approved leave count per teacher
    public List<Map<String, Object>> getTeacherLeaveUtilisation() {
        return leaveRepository.findLeaveCountByTeacher(LeaveStatus.APPROVED);
    }

    // FR9: Substitute burden — teachers assigned as substitute most often
    public List<Map<String, Object>> getSubstituteBurden() {
        return leaveRepository.findSubstituteBurdenReport(LeaveStatus.APPROVED);
    }

    // FR9: Timetable load per teacher — period count
    public List<Map<String, Object>> getTeacherWorkload() {
        return timetableRepository.findTeacherPeriodCount();
    }
}
