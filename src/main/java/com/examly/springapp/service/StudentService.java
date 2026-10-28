package com.examly.springapp.service;

import com.examly.springapp.dto.StudentRequestDTO;
import com.examly.springapp.entity.Student;
import com.examly.springapp.entity.User;
import com.examly.springapp.exception.DuplicateStudentException;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.StudentRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Student createStudent(StudentRequestDTO dto) {
        if (studentRepository.existsByRegisterNumber(dto.getRegisterNumber())) {
            throw new DuplicateStudentException("Student with register number " + dto.getRegisterNumber() + " already exists");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + dto.getUserId()));
        Student student = new Student(dto.getRegisterNumber(), dto.getDepartment(), dto.getYear(), user);
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    public Student getStudentByRegisterNumber(String registerNumber) {
        return studentRepository.findByRegisterNumber(registerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with register number: " + registerNumber));
    }

    @Transactional
    public Student updateStudent(Long id, StudentRequestDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        student.setDepartment(dto.getDepartment());
        student.setYear(dto.getYear());
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }
}
