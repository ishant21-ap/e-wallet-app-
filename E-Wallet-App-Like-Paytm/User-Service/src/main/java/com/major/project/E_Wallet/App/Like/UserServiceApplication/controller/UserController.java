package com.major.project.E_Wallet.App.Like.UserServiceApplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.dto.UserRequestDto;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.Users;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/addUpdate")
    public ResponseEntity<Users> addUpdate(@RequestBody @Valid UserRequestDto userRequestDto) throws JsonProcessingException {
        Users user = userService.addUpdate(userRequestDto);

        if(user != null) {
            ResponseEntity responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
            return responseEntity;
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/userDetails")
    public Users getUserDetails(@RequestParam("contact") String contact) {
        Users u = userService.loadUserByUsername(contact);
        return u;

    }
}
