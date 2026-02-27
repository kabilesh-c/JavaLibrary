package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Book entity operations
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    
    Optional<Book> findByIsbn(String isbn);
    
    boolean existsByIsbn(String isbn);
}
