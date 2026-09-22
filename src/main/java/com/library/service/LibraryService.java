package com.library.service;

import com.library.model.Book;
import com.library.model.Transaction;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    private static final double FINE_PER_DAY = 5.0; // R5 per day late

    // Constructor Injection (this removes ALL the @Autowired yellow lines)
    public LibraryService(BookRepository bookRepository, 
                          MemberRepository memberRepository, 
                          TransactionRepository transactionRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public boolean borrowBook(int bookId, int memberId, int daysToBorrow) {
        // 1. Check if book exists and is available
        Book book = bookRepository.findById(bookId);
        if (book == null || !book.isAvailable()) {
            return false;
        }

        // 2. Check if member exists
        try {
            memberRepository.findById(memberId);
        } catch (Exception e) {
            return false; // Member not found
        }

        // 3. Create transaction
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(daysToBorrow);
        transactionRepository.borrow(bookId, memberId, borrowDate, dueDate);

        // 4. Update book availability
        book.setAvailable(false);
        bookRepository.update(book);

        return true;
    }

    @Transactional
    public double returnBook(int transactionId) {
        // 1. Get the transaction
        Transaction transaction = transactionRepository.findById(transactionId);
        if (transaction == null || "RETURNED".equals(transaction.getStatus())) {
            return -1; // Invalid transaction or already returned
        }

        // 2. Calculate fine if overdue
        LocalDate returnDate = LocalDate.now();
        LocalDate dueDate = transaction.getDueDate();
        double fine = 0;

        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = daysLate * FINE_PER_DAY;
        }

        // 3. Update transaction
        transactionRepository.returnBook(transactionId, returnDate, fine);

        // 4. Make the book available again
        Book book = bookRepository.findById(transaction.getBookId());
        if (book != null) {
            book.setAvailable(true);
            bookRepository.update(book);
        }

        return fine;
    }
}