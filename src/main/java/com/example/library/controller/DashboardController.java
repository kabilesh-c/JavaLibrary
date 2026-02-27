package com.example.library.controller;

import com.example.library.service.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for Dashboard
 */
@Controller
public class DashboardController {

    private final LibraryService libraryService;

    public DashboardController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Check if logged in
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        // Get statistics for dashboard
        long totalBooks = libraryService.getAllBooks().size();
        long totalStudents = libraryService.getAllStudents().size();
        long activeIssues = libraryService.getActiveIssues().size();
        long totalIssues = libraryService.getAllIssues().size();

        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("activeIssues", activeIssues);
        model.addAttribute("totalIssues", totalIssues);

        return "dashboard";
    }
}
