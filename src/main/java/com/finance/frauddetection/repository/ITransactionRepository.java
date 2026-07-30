package com.finance.frauddetection.repository;

import org.springframework.stereotype.Component;

import java.util.List;
public interface ITransactionRepository {
    List<com.finance.frauddetection.models.Transaction> findAll();

    com.finance.frauddetection.models.Transaction findById(int id);

    int save(com.finance.frauddetection.models.Transaction transaction);

    void updateStatus(int id, String status);
}
