package com.finance.frauddetection.repository;

import com.finance.frauddetection.models.Customer;
import java.util.List;

public interface ICustomerRepository {

    List<Customer> getCustomers();

    Customer getCustomerById(int id);

}