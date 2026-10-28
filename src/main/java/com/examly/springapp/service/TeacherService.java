package com.examly.springapp.service;

import com.examly.springapp.dto.TeacherRequestDTO;
import com.examly.springapp.entity.Teacher;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.TeacherRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Teacher createTeacher(TeacherRequestDTO dto) {
        if (teacherRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new DuplicateStudentException("Teacher with employee ID " + dto.getEmployeeId() + " already exists");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));
        Teacher teacher = new Teacher(dto.getEmployeeId(), dto.getDesignation(), dto.getSubject(), user);
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + id));
    }

    public Teacher getTeacherByEmployeeId(String employeeId) {
        return teacherRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with employee ID: " + employeeId));
    }

    @Transactional
    public Teacher updateTeacher(Long id, TeacherRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + id));
        teacher.setDesignation(dto.getDesignation());
        teacher.setSubject(dto.getSubject());
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher not found with ID: " + id);
        }
        teacherRepository.deleteById(id);
    }
}
