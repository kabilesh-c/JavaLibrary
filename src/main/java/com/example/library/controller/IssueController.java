package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.Issue;
import com.example.library.entity.Student;
import com.example.library.service.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Issue/Return operations
 */
@Controller
@RequestMapping("/issues")
public class IssueController {

    private final LibraryService libraryService;

    public IssueController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Show issue book form
     */
    @GetMapping("/new")
    public String showIssueForm(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // Get available books (with available copies > 0)
        List<Book> books = libraryService.getAllBooks().stream()
                .filter(book -> book.getAvailableCopies() > 0)
                .toList();
        
        List<Student> students = libraryService.getAllStudents();

        model.addAttribute("books", books);
        model.addAttribute("students", students);
        
        // Default due date: 14 days from now
        model.addAttribute("defaultDueDate", LocalDate.now().plusDays(14));

        return "issues/issue_form";
    }

    /**
     * Process issue book request
     */
    @PostMapping("/issue")
    public String issueBook(@RequestParam UUID bookId,
                           @RequestParam UUID studentId,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            // For simplicity, staff ID is null (can be enhanced to track logged-in staff)
            libraryService.issueBook(bookId, studentId, null, dueDate);
            redirectAttributes.addFlashAttribute("message", "Book issued successfully");
            return "redirect:/issues/history";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/issues/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error issuing book: " + e.getMessage());
            return "redirect:/issues/new";
        }
    }

    /**
     * Show issue history
     */
    @GetMapping("/history")
    public String showHistory(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Issue> issues = libraryService.getAllIssues();
        model.addAttribute("issues", issues);
        model.addAttribute("activeOnly", false);

        return "issues/history";
    }

    /**
     * Show active issues only
     */
    @GetMapping("/active")
    public String showActiveIssues(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Issue> activeIssues = libraryService.getActiveIssues();
        model.addAttribute("issues", activeIssues);
        model.addAttribute("activeOnly", true);

        return "issues/history";
    }

    /**
     * Return a book
     */
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable UUID id,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            // Use today if no return date specified
            if (returnDate == null) {
                returnDate = LocalDate.now();
            }
            
            Issue returnedIssue = libraryService.returnBook(id, returnDate);
            
            String message = "Book returned successfully";
            if (returnedIssue.getFine().compareTo(java.math.BigDecimal.ZERO) > 0) {
                message += ". Fine: ₹" + returnedIssue.getFine();
            }
            
            redirectAttributes.addFlashAttribute("message", message);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error returning book: " + e.getMessage());
        }
        
        return "redirect:/issues/history";
    }
}
