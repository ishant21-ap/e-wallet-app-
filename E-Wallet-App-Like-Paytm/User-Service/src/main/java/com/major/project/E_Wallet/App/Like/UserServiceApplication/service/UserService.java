package com.major.project.E_Wallet.App.Like.UserServiceApplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.dto.UserRequestDto;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.Users;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.repository.UserRepository;
import com.major.project.E_Wallet.App.Like.Utilities.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.authority}")
    private String userAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public Users addUpdate(UserRequestDto userRequestDto) throws JsonProcessingException {
        Users user = userRequestDto.toUser();   //converting dto to user
        user.setAuthorities(userAuthority);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user = userRepository.save(user);
        //send a mail that the user has been created
        //simply push a message into kafka queue that user has been created

        //JSONOBject is a type of object which can be understanable by anyone (frontend/backend/postman) having key and value
        //I want to send only limited data from user class thats why we are using
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_CONTACT, user.getContact());
        jsonObject.put(CommonConstants.USER_EMAIL, user.getEmail());
        jsonObject.put(CommonConstants.USER_NAME, user.getName());
        jsonObject.put(CommonConstants.USER_IDENTIFIER, user.getUserIdentifier());
        jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE, user.getUserIdentifierValue());
        jsonObject.put(CommonConstants.USER_ID, user.getPk());

        logger.info("json object as a string: " + jsonObject);
        logger.info("json object as a string by object mapper " + objectMapper.writeValueAsString(jsonObject));
        kafkaTemplate.send(CommonConstants.USER_CREATED_TOPIC, objectMapper.writeValueAsString(jsonObject));   //converting JSON data to String with ObjectMapper (made in CommonConfig.class)
        return user;

    }

    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByContact(username);
        System.out.println("got the user details " + users);
        return users;

    }
}
