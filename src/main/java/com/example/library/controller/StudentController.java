package com.example.library.controller;

import com.example.library.entity.Student;
import com.example.library.service.LibraryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

/**
 * Controller for Student management
 */
@Controller
@RequestMapping("/students")
public class StudentController {

    private final LibraryService libraryService;

    public StudentController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * List all students
     */
    @GetMapping
    public String listStudents(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Student> students = libraryService.getAllStudents();
        model.addAttribute("students", students);
        return "students/list";
    }

    /**
     * Show form to add a new student
     */
    @GetMapping("/new")
    public String showAddStudentForm(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "students/form";
    }

    /**
     * Show form to edit an existing student
     */
    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable UUID id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            Student student = libraryService.getStudentById(id);
            model.addAttribute("student", student);
            model.addAttribute("isEdit", true);
            return "students/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Student not found: " + e.getMessage());
            return "redirect:/students";
        }
    }

    /**
     * Save student (create or update)
     */
    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute Student student, 
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", student.getId() != null);
            return "students/form";
        }

        try {
            libraryService.saveStudent(student);
            redirectAttributes.addFlashAttribute("message", "Student saved successfully");
            return "redirect:/students";
        } catch (Exception e) {
            model.addAttribute("error", "Error saving student: " + e.getMessage());
            model.addAttribute("isEdit", student.getId() != null);
            return "students/form";
        }
    }

    /**
     * Delete a student
     */
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable UUID id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            libraryService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("message", "Student deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/students";
    }
}
