package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByUsernameAndPassword(String userName, String password);
    Account findByUsername(String userName);
}
