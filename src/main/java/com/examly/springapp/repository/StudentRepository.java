package com.examly.springapp.repository;

import com.examly.springapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRegisterNumber(String registerNumber);

    Optional<Student> findByUserId(Long userId);

    boolean existsByRegisterNumber(String registerNumber);
}
