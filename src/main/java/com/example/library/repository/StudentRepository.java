package com.example.library.repository;

import com.example.library.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Student entity operations
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    
    Optional<Student> findByRoll(String roll);
    
    boolean existsByRoll(String roll);
    
    boolean existsByEmail(String email);
}
