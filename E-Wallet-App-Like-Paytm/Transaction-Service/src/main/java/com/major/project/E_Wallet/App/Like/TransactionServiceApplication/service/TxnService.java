package com.major.project.E_Wallet.App.Like.TransactionServiceApplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.project.E_Wallet.App.Like.TransactionServiceApplication.Repository.TxnRepo;
import com.major.project.E_Wallet.App.Like.TransactionServiceApplication.model.Txn;
import com.major.project.E_Wallet.App.Like.TransactionServiceApplication.model.TxnStatus;
import com.major.project.E_Wallet.App.Like.Utilities.CommonConstants;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TxnService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;    //RestTemplate help us with http calls

    @Autowired
    private TxnRepo txnRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Override
    public UserDetails loadUserByUsername(String userContact) throws UsernameNotFoundException {
        //Setting up the auth we are using in postman (basic auth)
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("txn-service", "txn-service");   //adding the username and password we have set in User-service modules main method
        //Now this header need to be pass in request entity in restemplate.exchange(.....  so we are making HttpEntity
        HttpEntity reqEntity = new HttpEntity(httpHeaders);
        JSONObject  object = restTemplate.exchange
                ("http://localhost:8081/user/userDetails?contact="+userContact,
                        HttpMethod.GET, reqEntity, JSONObject.class).getBody();

        List<LinkedHashMap<String, String>> list =  (List<LinkedHashMap<String, String>>)(object.get("authorities"));
        List<GrantedAuthority> reqAuthorities = list.stream().map(x -> x.get("authority")).map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
        User user = new User((String) object.get("username"), (String) object.get("password"), reqAuthorities);
        return user;

    }

    public String initTxn(String receiver, String purpose, String amount, String sender) throws JsonProcessingException {

        Txn txn  = Txn.builder()
                .txnId(UUID.randomUUID().toString()).amount(Double.valueOf(amount))
                .receiver(receiver)
                .sender(sender)
                .status(TxnStatus.INITIATED)
                .purpose(purpose).build();
        txnRepo.save(txn);

        JSONObject obj = new JSONObject();
        obj.put(CommonConstants.AMOUNT, txn.getAmount());
        obj.put(CommonConstants.RECEIVER, txn.getReceiver());
        obj.put(CommonConstants.SENDER, txn.getSender());
        obj.put(CommonConstants.TXNID, txn.getTxnId());
        obj.put(CommonConstants.PURPOSE, txn.getPurpose());
        obj.put(CommonConstants.STATUS, txn.getStatus());
        kafkaTemplate.send(CommonConstants.TXN_INITIATED_TOPIC, objectMapper.writeValueAsString(obj));

        return txn.getTxnId();
    }
}
