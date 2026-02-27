package com.example.library.repository;

import com.example.library.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Staff entity operations
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    
    Optional<Staff> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
