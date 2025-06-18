package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.AccountService;
import com.example.service.MessageService;


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
    public ResponseEntity<Object> userRegistration(@RequestBody String account){
        String json = "";
        try {
            json = accountService.userRegistration(account);
            if(json.equals("")){
                return ResponseEntity.status(400).body(json);
            } else {
                return ResponseEntity.status(200).body(json);
            }
        }catch (Exception e){
            return ResponseEntity.status(409).body(json);
        }
    }

    //user login
    @PostMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestBody String account){
        String json = accountService.userLogin(account);
        if(json.equals("")){
            return ResponseEntity.status(401).body(json);
        } else {
            return ResponseEntity.status(200).body(json);
        }
    }

    //create message
    @PostMapping("/messages")
    public ResponseEntity<Object> createMessage(@RequestBody String message){
        String json = messageService.createMessage(message);
        if(json.equals("")){
            return ResponseEntity.status(400).body(json);
        } else {
            return ResponseEntity.status(200).body(json);
        }
    }

    //get messages by account id
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<Object> getMessagesById(@PathVariable Integer account_id){
        return ResponseEntity.status(200).body(messageService.getMessagesById(account_id));
    }

    //get all messages
    @GetMapping("/messages")
    public ResponseEntity<Object> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    //get message by message id
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Object> getMessageById(@PathVariable Integer message_id){
        return ResponseEntity.status(200).body(messageService.getMessageById(message_id));
    }

    //update message by message id
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<String> updateMessage(@PathVariable Integer message_id, @RequestBody String messageText){
        String json = messageService.updateMessage(message_id, messageText);
        if(json.equals("")){
            return ResponseEntity.status(400).body(json);
        } else {
            return ResponseEntity.status(200).body(json);
        }
    }

    //delete message by message id
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable Integer message_id){
        Long response = messageService.deleteMessage(message_id);
        if(response == 1){
            return ResponseEntity.status(200).body(response);
        } else {
            return ResponseEntity.status(200).body("");
        }
    }



}
