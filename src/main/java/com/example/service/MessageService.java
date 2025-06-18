package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;
    private ObjectMapper om = new ObjectMapper();

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){

        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public String createMessage(String obj) {
        String json = "";
        try {
            Message message = om.readValue(obj, Message.class);
            if(message.getMessageText() != "" && message.getMessageText().length() <= 255) {
                accountRepository.findById(message.getPostedBy());
                message = (Message) messageRepository.save(message);
                json = om.writeValueAsString(message);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getMessagesById(Integer account_id) {
        String json = "";
         try {
            Account account = accountRepository.findById(account_id).get();
            if(account != null){
            List<Message> messages = (List<Message>) messageRepository.findAllByPostedBy(account_id);
            json = om.writeValueAsString(messages);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getAllMessages() {
        String json = "";
        List<Message> messages = (List<Message>) messageRepository.findAll();
        try {
            json = om.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getMessageById(Integer message_id) {
        String json = "";
        try {
            Message message = (Message) messageRepository.findById(message_id).get();
            json = om.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String updateMessage(Integer message_id, String messageText) {
        String json = "";
        String newText = messageText.substring(messageText.indexOf("=") + 1, messageText.length());
        JsonObject obj = JsonParser.parseString(newText).getAsJsonObject();
        String text = obj.get("messageText").toString().replace("\"", "");
        try {
            Message message = messageRepository.findById(message_id).get();
            if(message != null) {
                if(!text.equals("") && text.length() <= 255){
                    message.setMessageText(text);
                    message = messageRepository.save(message);
                    json = "1";
                }
            }        
        } catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public Long deleteMessage(Integer message_id) {
        Long result = 0L;
        try {
            messageRepository.deleteById(message_id);
            result = 1L;
        } catch (Exception e){
            result = 0L;
        }
        return result;
    }
}
