package com.major.project.E_Wallet.App.Like.NotificationServiceApplication.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.project.E_Wallet.App.Like.Utilities.CommonConstants;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedConsumer {

    private static Logger logger = LoggerFactory.getLogger(UserCreatedConsumer.class);


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSender javaMailSender;

    //whenever we want to listen something from topic we use kafkaListner
    @KafkaListener(topics = {CommonConstants.USER_CREATED_TOPIC}, groupId = "notification-group")
    public void sendNotification(String message) throws JsonProcessingException {
        //Converting String message into JSON object
        JSONObject jsonObject = objectMapper.readValue(message, JSONObject.class);   //Reading message in the form of JSON

        String name = (String) jsonObject.get(CommonConstants.USER_NAME);
        String email = (String) jsonObject.get(CommonConstants.USER_EMAIL);

        //send a mail
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText("Welcome " + name + " to the platform.");
        simpleMailMessage.setSubject("EWallet User Created | " + name);
        simpleMailMessage.setFrom("ewalletjbdl80@gmail.com");
        //If you want to send message with some attachment then use MiMe Message
        //Now we have created a message now lets send it
        javaMailSender.send(simpleMailMessage);

        logger.info("Mail has been sent to the User !");

    }
}
