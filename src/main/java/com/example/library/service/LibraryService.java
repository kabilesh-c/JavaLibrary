package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Issue;
import com.example.library.entity.Staff;
import com.example.library.entity.Student;
import com.example.library.repository.BookRepository;
import com.example.library.repository.IssueRepository;
import com.example.library.repository.StaffRepository;
import com.example.library.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Service layer for Library Management business logic
 */
@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final IssueRepository issueRepository;

    public LibraryService(BookRepository bookRepository, 
                         StudentRepository studentRepository,
                         StaffRepository staffRepository, 
                         IssueRepository issueRepository) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.issueRepository = issueRepository;
    }

    // Book operations
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + id));
    }

    @Transactional
    public Book saveBook(Book book) {
        // For new books, set available copies equal to total copies
        if (book.getId() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        Book book = getBookById(id);
        // Check if book has active issues
        List<Issue> activeIssues = issueRepository.findByBookId(id).stream()
                .filter(issue -> "issued".equalsIgnoreCase(issue.getStatus()))
                .toList();
        if (!activeIssues.isEmpty()) {
            throw new IllegalStateException("Cannot delete book with active issues");
        }
        bookRepository.delete(book);
    }

    // Student operations
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with ID: " + id));
    }

    @Transactional
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(UUID id) {
        Student student = getStudentById(id);
        // Check if student has active issues
        List<Issue> activeIssues = issueRepository.findActiveIssuesByStudent(id);
        if (!activeIssues.isEmpty()) {
            throw new IllegalStateException("Cannot delete student with active book issues");
        }
        studentRepository.delete(student);
    }

    // Staff operations
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Staff not found with ID: " + id));
    }

    @Transactional
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    // Issue operations
    public List<Issue> getAllIssues() {
        return issueRepository.findAllOrderedByIssueDateDesc();
    }

    public Issue getIssueById(UUID id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Issue not found with ID: " + id));
    }

    public List<Issue> getActiveIssues() {
        return issueRepository.findByStatus("issued");
    }

    /**
     * Issue a book to a student
     * @param bookId Book UUID
     * @param studentId Student UUID
     * @param staffId Staff UUID (can be null)
     * @param dueDate Due date for return
     * @return Created Issue entity
     * @throws NoSuchElementException if book or student not found
     * @throws IllegalStateException if no copies available
     */
    @Transactional
    public Issue issueBook(UUID bookId, UUID studentId, UUID staffId, LocalDate dueDate) {
        // Fetch book and validate availability
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for this book");
        }

        // Fetch student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found"));

        // Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create issue record
        Issue issue = new Issue();
        issue.setBook(book);
        issue.setStudent(student);
        issue.setStaffId(staffId);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(dueDate);
        issue.setStatus("issued");
        issue.setFine(BigDecimal.ZERO);

        return issueRepository.save(issue);
    }

    /**
     * Return a book
     * @param issueId Issue UUID
     * @param returnDate Actual return date
     * @return Updated Issue entity with fine calculated
     * @throws NoSuchElementException if issue not found
     * @throws IllegalStateException if already returned
     */
    @Transactional
    public Issue returnBook(UUID issueId, LocalDate returnDate) {
        // Fetch issue
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("Issue record not found"));

        // Check if already returned
        if ("returned".equalsIgnoreCase(issue.getStatus())) {
            throw new IllegalStateException("Book already returned");
        }

        // Set return date and status
        issue.setReturnDate(returnDate);
        issue.setStatus("returned");

        // Calculate fine: 10 per day late
        if (issue.getDueDate() != null && returnDate.isAfter(issue.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(issue.getDueDate(), returnDate);
            BigDecimal fineAmount = BigDecimal.valueOf(daysLate * 10);
            issue.setFine(fineAmount);
        } else {
            issue.setFine(BigDecimal.ZERO);
        }

        // Increment available copies
        Book book = issue.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return issueRepository.save(issue);
    }

    /**
     * Check and update overdue issues
     */
    @Transactional
    public void updateOverdueStatus() {
        LocalDate today = LocalDate.now();
        List<Issue> activeIssues = issueRepository.findByStatus("issued");
        
        for (Issue issue : activeIssues) {
            if (issue.getDueDate() != null && today.isAfter(issue.getDueDate())) {
                issue.setStatus("overdue");
                issueRepository.save(issue);
            }
        }
    }
}
