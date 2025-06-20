package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){

        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) throws ResourceNotFoundException{
        if(message.getMessageText() != "") {
            if(message.getMessageText().length() <= 255) {
            accountRepository.findById(message.getPostedBy()).orElseThrow(() ->
                new ResourceNotFoundException("Account ID " + message.getPostedBy() + " not found."));
            return messageRepository.save(message);
            } else {
                throw new IllegalArgumentException("Message Text is too long.");
            }
        } else {
            throw new IllegalArgumentException("Message Text can not be blank.");
        }
    }

    public List<Message> getMessagesById(Integer account_id) throws ResourceNotFoundException {
        accountRepository.findById(account_id).orElseThrow(() ->
                    new ResourceNotFoundException("Account ID " + account_id + " not found."));
        return messageRepository.findAllByPostedBy(account_id);
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageById(Integer message_id) throws NoSuchElementException {
        return messageRepository.findById(message_id).orElseThrow(() -> new NoSuchElementException(""));
    }

    public Integer updateMessage(Integer message_id, String messageText) throws ResourceNotFoundException {
        Integer itemsChanged = 0;
        String newText = messageText.substring(messageText.indexOf("=") + 1, messageText.length());
        JsonObject obj = JsonParser.parseString(newText).getAsJsonObject();
        String text = obj.get("messageText").toString().replace("\"", "");
        Message message = messageRepository.findById(message_id).orElseThrow(() ->
                    new ResourceNotFoundException("Message ID " + message_id + " not found."));
        if(!text.equals("")){
            if(text.length() <= 255){
            message.setMessageText(text);
            message = messageRepository.save(message);
            itemsChanged = 1;
            } else {
                throw new IllegalArgumentException("Message text must not be blank");
            }
        } else {
            throw new IllegalArgumentException("Message text must not be blank");
        }
        return itemsChanged; 
    }

    public long deleteMessage(Integer message_id){
        Long origCount = messageRepository.count();
        if(messageRepository.existsById(message_id)) {
            messageRepository.deleteById(message_id);
        }
        Long newCount = messageRepository.count();
        return origCount - newCount;
    }
}
