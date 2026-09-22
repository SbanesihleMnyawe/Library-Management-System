package com.library.controller;

import com.library.model.Transaction;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import com.library.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LibraryService libraryService;

    // Constructor Injection (this removes ALL the @Autowired yellow lines)
    public TransactionController(TransactionRepository transactionRepository,
                                 BookRepository bookRepository,
                                 MemberRepository memberRepository,
                                 LibraryService libraryService) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.libraryService = libraryService;
    }

    @GetMapping
    public String listTransactions(Model model) {
        List<Transaction> transactions = transactionRepository.findAll();
        model.addAttribute("transactions", transactions);
        
        // For the borrow form dropdowns
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        return "transaction-list";
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam int bookId, 
                             @RequestParam int memberId, 
                             @RequestParam int days) {
        boolean success = libraryService.borrowBook(bookId, memberId, days);
        if (success) {
            return "redirect:/transactions?success=Borrowed successfully";
        } else {
            return "redirect:/transactions?error=Book unavailable or Member invalid";
        }
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam int transactionId) {
        double fine = libraryService.returnBook(transactionId);
        if (fine >= 0) {
            return "redirect:/transactions?success=Returned. Fine: R" + fine;
        } else {
            return "redirect:/transactions?error=Invalid transaction or already returned";
        }
    }
}