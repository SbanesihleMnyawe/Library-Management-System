package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    // Constructor Injection (removes @Autowired yellow line)
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "book-list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping
    public String addBook(@ModelAttribute Book book) {
        book.setAvailable(true);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
public String showEditForm(@PathVariable int id, Model model) {
    model.addAttribute("book", bookRepository.findById(id));
    return "book-form"; // Reuse the same form!
}

@PostMapping("/update/{id}")
public String updateBook(@PathVariable int id, @ModelAttribute Book book) {
    book.setBookId(id);
    bookRepository.update(book);
    return "redirect:/books";
}

}