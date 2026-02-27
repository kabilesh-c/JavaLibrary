package com.example.library.repository;

import com.example.library.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * Repository for Issue entity operations
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID> {
    
    List<Issue> findByStatus(String status);
    
    List<Issue> findByStudentId(UUID studentId);
    
    List<Issue> findByBookId(UUID bookId);
    
    @Query("SELECT i FROM Issue i WHERE i.student.id = ?1 AND i.status = 'issued'")
    List<Issue> findActiveIssuesByStudent(UUID studentId);
    
    @Query("SELECT i FROM Issue i ORDER BY i.issueDate DESC")
    List<Issue> findAllOrderedByIssueDateDesc();
}
