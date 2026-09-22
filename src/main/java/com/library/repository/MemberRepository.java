package com.library.repository;

import com.library.model.Member;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor Injection (this removes the @Autowired yellow line)
    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "SELECT member_id as memberId, name, email, phone, membership_date as membershipDate FROM Members";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Member.class));
    }

    public Member findById(int id) {
        String sql = "SELECT member_id as memberId, name, email, phone, membership_date as membershipDate FROM Members WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Member.class), id);
    }

    public void save(Member member) {
        String sql = "INSERT INTO Members (name, email, phone) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getName(), member.getEmail(), member.getPhone());
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM Members WHERE member_id = ?";
        jdbcTemplate.update(sql, id);
    }
}