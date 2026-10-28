package com.examly.springapp.service;

import com.examly.springapp.entity.Attendance;
import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.entity.Student;
import com.examly.springapp.repository.AttendanceRepository;
import com.examly.springapp.repository.LeaveApplicationRepository;
import com.examly.springapp.repository.StudentRepository;
import com.examly.springapp.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MisReportService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final LeaveApplicationRepository leaveRepository;
    private final TimetableRepository timetableRepository;

    public MisReportService(AttendanceRepository attendanceRepository,
                            StudentRepository studentRepository,
                            LeaveApplicationRepository leaveRepository,
                            TimetableRepository timetableRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.leaveRepository = leaveRepository;
        this.timetableRepository = timetableRepository;
    }

    // FR8: Daily attendance register for a given date
    public List<Attendance> getDailyAttendanceRegister(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    // FR8: Below-75% student list with student details for regulatory inspection
    public List<Student> getBelow75StudentList() {
        List<Long> studentIds = attendanceRepository.findStudentIdsBelow75Percent();
        return studentRepository.findAllById(studentIds);
    }

    // FR8: Annual attendance summary per student
    public List<Map<String, Object>> getAnnualAttendanceSummary() {
        return attendanceRepository.findAnnualAttendanceSummary();
    }

    // FR8: Teacher deployment report — period count per teacher
    public List<Map<String, Object>> getTeacherDeploymentReport() {
        return timetableRepository.findTeacherPeriodCount();
    }

    // FR8: Leave summary per teacher for HR records
    public List<LeaveApplication> getAnnualLeaveSummary(Long teacherId) {
        return leaveRepository.findByTeacherId(teacherId);
    }
}
