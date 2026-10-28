package com.examly.springapp.service;

import com.examly.springapp.dto.AttendanceRequestDTO;
import com.examly.springapp.entity.Attendance;
import com.examly.springapp.entity.Attendance.CaptureMethod;
import com.examly.springapp.entity.Attendance.Status;
import com.examly.springapp.entity.Student;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.AttendanceOverrideException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.exception.UnauthorisedAccessException;
import com.examly.springapp.repository.AttendanceRepository;
import com.examly.springapp.repository.StudentRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Attendance markAttendance(AttendanceRequestDTO dto, Long recordedByUserId) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + dto.getStudentId()));
        User recorder = userRepository.findById(recordedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + recordedByUserId));

        Attendance attendance = new Attendance(
                dto.getDate(),
                Status.valueOf(dto.getStatus()),
                dto.getCapturedBy() != null ? CaptureMethod.valueOf(dto.getCapturedBy()) : CaptureMethod.MANUAL,
                student,
                recorder
        );
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance overrideAttendance(Long attendanceId, String newStatus, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new AttendanceOverrideException("Reason is required for attendance modification");
        }
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + attendanceId));
        attendance.setStatus(Status.valueOf(newStatus));
        attendance.setModificationReason(reason);
        attendance.setModifiedAt(java.time.LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    // FR3: data scoping — STUDENT role may only view their own attendance
    public List<Attendance> getStudentAttendance(Long studentId, String requestingUserEmail, String requestingUserRole) {
        if ("STUDENT".equalsIgnoreCase(requestingUserRole)) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
            if (!student.getUser().getEmail().equalsIgnoreCase(requestingUserEmail)) {
                throw new UnauthorisedAccessException("Access denied: students may only view their own attendance");
            }
        }
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getClassAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public List<Long> getStudentsBelow75Percent() {
        return attendanceRepository.findStudentIdsBelow75Percent();
    }
}
