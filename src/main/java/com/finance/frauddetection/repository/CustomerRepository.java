package com.finance.frauddetection.repository;

import com.finance.frauddetection.models.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository implements ICustomerRepository {
    private List<Customer> customers = new ArrayList<>();
    public CustomerRepository() {
        customers.add(new Customer(1, "Rahul Sharma", "ACC1001", "India"));
        customers.add(new Customer(2, "Priya Verma", "ACC1002", "India"));
        customers.add(new Customer(3, "John Smith", "ACC1003", "USA"));
    }
    @Override
    public List<Customer> getCustomers() {
        return customers;
    }
    @Override
    public Customer getCustomerById(int id) {
        return customers.stream().filter(customer -> customer.getId() == id)
                .findFirst().orElse(null);
    }
}