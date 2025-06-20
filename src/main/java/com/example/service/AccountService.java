package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account userRegistration(Account account) throws IllegalArgumentException {
        if(account.getUsername() != ""){ 
            if(account.getPassword().length() >= 4) {
                return accountRepository.save(account);
            } else {
                throw new IllegalArgumentException(account.getPassword() + " is not valid, please check and try again");
            }
        } else {
            throw new IllegalArgumentException(account.getUsername() + " is not valid, please check and try again");
        }
    }

    public Account userLogin(Account account) {
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
