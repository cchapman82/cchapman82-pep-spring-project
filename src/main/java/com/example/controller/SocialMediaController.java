package com.example.controller;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService){

        this.messageService = messageService;
        this.accountService = accountService;

    }
    //user registration
    @PostMapping("/register")
    public ResponseEntity<Account> userRegistration(@RequestBody Account account) {
            account = accountService.userRegistration(account);
            if(account == null){
                return ResponseEntity.status(400).body(null);
            } else {
                return ResponseEntity.status(200).body(account);
            }
    }

    //user login
    @PostMapping("/login")
    public ResponseEntity<Account> userLogin(@RequestBody Account account){
        account = accountService.userLogin(account);
        if(account == null){
            return ResponseEntity.status(401).body(null);
        } else {
            return ResponseEntity.status(200).body(account);
        }
    }

    //create message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws ResourceNotFoundException {
        message = messageService.createMessage(message);
        if(message == null){
            return ResponseEntity.status(400).body(null);
        } else {
            return ResponseEntity.status(200).body(message);
        }
    }

    //get messages by account id
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesById(@PathVariable Integer account_id) throws ResourceNotFoundException {
        return ResponseEntity.status(200).body(messageService.getMessagesById(account_id));
    }

    //get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    //get message by message id
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) throws NoSuchElementException {
        return ResponseEntity.ok().body(messageService.getMessageById(message_id)); 
    }

    //update message by message id
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody String messageText) 
                                                                                        throws ResourceNotFoundException {
        Integer itemsChanged = messageService.updateMessage(message_id, messageText);
        if(itemsChanged == 1){
            return ResponseEntity.status(200).body(itemsChanged);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    //delete message by message id
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Long> deleteMessage(@PathVariable Integer message_id) throws NoSuchElementException {
        Long response = messageService.deleteMessage(message_id);
        if(response == 1){
            return ResponseEntity.status(200).body(response);
        } else {
            return ResponseEntity.status(200).body(null);
        }
    }

    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String confilctHandler(){ return "Username already in use, please try again";}

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentHandler(IllegalArgumentException ex){ return ex.getMessage();}

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String resourceNotFoundHandler(ResourceNotFoundException ex){ return ex.getMessage();}

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.OK)
    public String noSuchElementHandler(NoSuchElementException ex){ return "";}
}
