package com.finance.frauddetection.repository;

import com.finance.frauddetection.models.Transaction;
import com.finance.frauddetection.repository.ITransactionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Profile("JDBC")
public class SQLTransactionRepository implements ITransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Transaction> rowMapper = (ResultSet rs, int rowNum) -> new Transaction(
            rs.getInt("id"),
            rs.getInt("customer_id"),
            rs.getBigDecimal("amount"),
            rs.getString("txn_country"),
            rs.getTimestamp("txn_timestamp").toLocalDateTime(),
            rs.getString("status")
    );

    public SQLTransactionRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query("SELECT * FROM transactions ORDER BY txn_timestamp DESC", rowMapper);
    }

    @Override
    public Transaction findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM transactions WHERE id = ?", rowMapper, id);
    }

    @Override
    public int save(Transaction transaction) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // Fixed: Added status to column list so it matches 5 placeholders
        String sql = "INSERT INTO transactions (customer_id, amount, txn_country, txn_timestamp, status) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, transaction.getCustomerId());
            ps.setBigDecimal(2, transaction.getAmount());
            ps.setString(3, transaction.getTxnCountry());
            ps.setTimestamp(4, Timestamp.valueOf(transaction.getTxnTimestamp()));
            ps.setString(5, transaction.getStatus());
            return ps;
        }, keyHolder); // Fixed: Pass keyHolder as the 2nd argument to jdbcTemplate.update()

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public void updateStatus(int id, String status) {
        // Fixed: Added missing '?' placeholder and fixed table name to 'transactions'
        jdbcTemplate.update("UPDATE transactions SET status = ? WHERE id = ?", status, id);
    }
}