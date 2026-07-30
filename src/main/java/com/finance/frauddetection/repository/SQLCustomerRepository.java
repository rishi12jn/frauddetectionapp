package com.finance.frauddetection.repository;

import com.finance.frauddetection.models.Customer;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
@Profile("JDBC")
public class SQLCustomerRepository implements ICustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Customer> rowMapper = (ResultSet rs, int rowNum) -> new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("account_number"),
            rs.getString("country")
    );

    public SQLCustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> getCustomers() {
        return jdbcTemplate.query("SELECT * FROM customers ORDER BY id DESC", rowMapper);
    }

    @Override
    public Customer getCustomerById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM customers WHERE id = ?", rowMapper, id);
    }
}