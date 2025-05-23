package com.major.project.E_Wallet.App.Like.WalletServiceApplication.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.project.E_Wallet.App.Like.Utilities.CommonConstants;
import com.major.project.E_Wallet.App.Like.WalletServiceApplication.model.Wallet;
import com.major.project.E_Wallet.App.Like.WalletServiceApplication.repository.WalletRepo;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedConsumer {

    private static Logger logger = LoggerFactory.getLogger(UserCreatedConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${user.creation.time.balance}")
    private double balance;

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = CommonConstants.USER_CREATED_TOPIC, groupId = "wallet-group")
    public void createWallet(String message) throws JsonProcessingException {
        JSONObject object = objectMapper.readValue(message, JSONObject.class);
        Integer userId = (Integer) object.get(CommonConstants.USER_ID);
        String contact = (String) object.get(CommonConstants.USER_CONTACT);


        //Building wallet object
        Wallet wallet = Wallet.builder()
                .contact(contact)
                .userId(userId)
                .balance(balance)
                .build();
        walletRepo.save(wallet);
        logger.info("Wallet has beem created for the user. ");


        //Now in order to send mail to the user that his wallet has been created we have to make this Producer and generate message
        JSONObject object1 = new JSONObject();
        object1.put(CommonConstants.USER_ID, userId);
        object1.put(CommonConstants.WALLET_BALANCE, balance);
        kafkaTemplate.send(CommonConstants.WALLET_CREATED_TOPIC, objectMapper.writeValueAsString(object1));
        logger.info("Produced the wallet creation message in the queue for user id " + userId);
    }
}
