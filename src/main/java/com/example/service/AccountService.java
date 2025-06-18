package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private ObjectMapper om = new ObjectMapper();

    @Autowired
    public AccountService(AccountRepository accountRepository){

        this.accountRepository = accountRepository;

    }

    public String userRegistration(String obj) throws Exception {
        String json = "";
        try {
            Account account = om.readValue(obj, Account.class);
            if(account.getUsername() != "" && account.getPassword().length() >= 4) {
                account = (Account) accountRepository.save(account);
                json = om.writeValueAsString(account);
            } 
        }catch(JsonProcessingException e) {
                e.printStackTrace();
        }
        return json;
    }

    public String userLogin(String obj) {
        String json = "";
        try {
            Account account = om.readValue(obj, Account.class);
            account = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
            if(account != null) {
                json = om.writeValueAsString(account);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
