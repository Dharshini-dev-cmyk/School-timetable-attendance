package com.examly.springapp.service;

import com.examly.springapp.dto.LeaveRequestDTO;
import com.examly.springapp.entity.LeaveApplication;
import com.examly.springapp.entity.LeaveApplication.LeaveStatus;
import com.examly.springapp.entity.LeaveApplication.LeaveType;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.LeaveApplicationRepository;
import com.examly.springapp.repository.TeacherRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveApplicationRepository leaveRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public LeaveService(LeaveApplicationRepository leaveRepository,
                        TeacherRepository teacherRepository,
                        UserRepository userRepository) {
        this.leaveRepository = leaveRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LeaveApplication applyLeave(Long teacherId, LeaveRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + teacherId));
        LeaveApplication leave = new LeaveApplication(
                LeaveType.valueOf(dto.getLeaveType()),
                dto.getFromDate(),
                dto.getToDate(),
                dto.getReason(),
                teacher
        );
        return leaveRepository.save(leave);
    }

    public List<LeaveApplication> getPendingLeaves() {
        return leaveRepository.findByStatus(LeaveStatus.PENDING);
    }

    @Transactional
    public LeaveApplication approveLeave(Long leaveId, Long approvedByUserId, Long substituteTeacherId) {
        LeaveApplication leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with ID: " + leaveId));
        User approver = userRepository.findById(approvedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + approvedByUserId));
        Teacher substitute = teacherRepository.findById(substituteTeacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Substitute teacher not found with ID: " + substituteTeacherId));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(approver);
        leave.setSubstituteTeacher(substitute);
        return leaveRepository.save(leave);
    }

    @Transactional
    public LeaveApplication rejectLeave(Long leaveId, Long rejectedByUserId) {
        LeaveApplication leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with ID: " + leaveId));
        User approver = userRepository.findById(rejectedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + rejectedByUserId));
        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedBy(approver);
        return leaveRepository.save(leave);
    }

    public List<LeaveApplication> getTeacherLeaves(Long teacherId) {
        return leaveRepository.findByTeacherId(teacherId);
    }
}
