package com.major.project.E_Wallet.App.Like.WalletServiceApplication.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.project.E_Wallet.App.Like.Utilities.CommonConstants;
import com.major.project.E_Wallet.App.Like.WalletServiceApplication.model.Wallet;
import com.major.project.E_Wallet.App.Like.WalletServiceApplication.repository.WalletRepo;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TxnInitiatedConsumer {

    @Value("${wallet-group-id}")
    private String walletGroupId;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = CommonConstants.TXN_INITIATED_TOPIC, groupId = "wallet-group")
    public void updateWallet(String msg) throws JsonProcessingException {
        JSONObject obj = objectMapper.readValue(msg, JSONObject.class);
        String sender = (String) obj.get(CommonConstants.SENDER);
        String receiver = (String) obj.get(CommonConstants.RECEIVER);
        Double amount = (Double) obj.get(CommonConstants.AMOUNT);
        String purpose = (String) obj.get(CommonConstants.PURPOSE);
        String txnId = (String) obj.get(CommonConstants.TXNID);


        //checking whether sender and receiver has wallet associated or not
        //sender
        Wallet senderWallet = walletRepo.findByContact(sender);

        //receiver
        Wallet receiverWallet = walletRepo.findByContact(receiver);

        //publishing a message, by just looking at this TxnService will understand whether txn is done or not
        String message = "txn is initiated";
        String status = "pending";

        if(senderWallet == null){
            message = "sender wallet is not associated with us !";
            status = "failed";
        }
        else if(receiverWallet == null){
            message = "receiver wallet is not associated with us !";
            status = "failed";
        }
        else if(amount > senderWallet.getBalance()){
            message = "amount is greater than sender balance !";
            status = "failed";
        }
        else{
            walletRepo.updateWallet(sender, -amount);
            walletRepo.updateWallet(receiver, +amount);
            message = "txn is success";
            status = "success";
        }

        JSONObject response = new JSONObject();
        response.put(CommonConstants.STATUS, status);
        response.put(CommonConstants.MESSAGE, message);
        response.put(CommonConstants.TXNID, txnId);

        kafkaTemplate.send(CommonConstants.TXN_UPDATED_TOPIC, response);

    }
}
