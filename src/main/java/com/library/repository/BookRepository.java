package com.library.repository;

import com.library.model.Book;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor Injection (this removes the @Autowired yellow line)
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        String sql = "SELECT book_id as bookId, title, author, isbn, publication_year as publicationYear, available FROM Books";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
    }

    public Book findById(int id) {
        String sql = "SELECT book_id as bookId, title, author, isbn, publication_year as publicationYear, available FROM Books WHERE book_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), id);
    }

    public void save(Book book) {
        String sql = "INSERT INTO Books (title, author, isbn, publication_year, available) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationYear(), book.isAvailable());
    }

    public void update(Book book) {
    String sql = "UPDATE Books SET title=?, author=?, isbn=?, publication_year=? WHERE book_id=?";
    jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationYear(), book.getBookId());
}

    public void deleteById(int id) {
        String sql = "DELETE FROM Books WHERE book_id = ?";
        jdbcTemplate.update(sql, id);
    }
}