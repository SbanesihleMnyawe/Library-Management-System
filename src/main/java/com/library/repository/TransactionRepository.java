package com.library.repository;

import com.library.model.Transaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor Injection (this removes the @Autowired yellow line)
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> findAll() {
        String sql = "SELECT transaction_id as transactionId, book_id as bookId, member_id as memberId, " +
                     "borrow_date as borrowDate, due_date as dueDate, return_date as returnDate, fine, status " +
                     "FROM Transactions ORDER BY transaction_id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Transaction.class));
    }

    public void borrow(int bookId, int memberId, LocalDate borrowDate, LocalDate dueDate) {
        String sql = "INSERT INTO Transactions (book_id, member_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, 'BORROWED')";
        jdbcTemplate.update(sql, bookId, memberId, borrowDate, dueDate);
    }

    public void returnBook(int transactionId, LocalDate returnDate, double fine) {
        String sql = "UPDATE Transactions SET return_date = ?, fine = ?, status = 'RETURNED' WHERE transaction_id = ?";
        jdbcTemplate.update(sql, returnDate, fine, transactionId);
    }

    public Transaction findById(int id) {
        String sql = "SELECT transaction_id as transactionId, book_id as bookId, member_id as memberId, " +
                     "borrow_date as borrowDate, due_date as dueDate, return_date as returnDate, fine, status " +
                     "FROM Transactions WHERE transaction_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Transaction.class), id);
    }
}