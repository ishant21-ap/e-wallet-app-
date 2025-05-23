package com.major.project.E_Wallet.App.Like.TransactionServiceApplication.consumer;

import com.major.project.E_Wallet.App.Like.Utilities.CommonConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TxnUpdateConsumer {

    @KafkaListener(topics = CommonConstants.TXN_UPDATED_TOPIC, groupId = "txn-group")
    public void updateTxn(String msg){
        //if status is success/failed --> update the db (txn)
        //send notification
        System.out.println("I got the msg here");
    }
}
