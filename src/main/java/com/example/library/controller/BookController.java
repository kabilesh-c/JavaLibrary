package com.example.library.controller;

import com.example.library.entity.Book;
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
 * Controller for Book management
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private final LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * List all books
     */
    @GetMapping
    public String listBooks(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Book> books = libraryService.getAllBooks();
        model.addAttribute("books", books);
        return "books/list";
    }

    /**
     * Show form to add a new book
     */
    @GetMapping("/new")
    public String showAddBookForm(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        model.addAttribute("book", new Book());
        model.addAttribute("isEdit", false);
        return "books/form";
    }

    /**
     * Show form to edit an existing book
     */
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable UUID id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            Book book = libraryService.getBookById(id);
            model.addAttribute("book", book);
            model.addAttribute("isEdit", true);
            return "books/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Book not found: " + e.getMessage());
            return "redirect:/books";
        }
    }

    /**
     * Save book (create or update)
     */
    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute Book book, 
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", book.getId() != null);
            return "books/form";
        }

        try {
            libraryService.saveBook(book);
            redirectAttributes.addFlashAttribute("message", "Book saved successfully");
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("error", "Error saving book: " + e.getMessage());
            model.addAttribute("isEdit", book.getId() != null);
            return "books/form";
        }
    }

    /**
     * Delete a book
     */
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable UUID id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            libraryService.deleteBook(id);
            redirectAttributes.addFlashAttribute("message", "Book deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting book: " + e.getMessage());
        }
        return "redirect:/books";
    }
}
