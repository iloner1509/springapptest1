package com.example.springapptest.controller;

import com.example.springapptest.dto.UserDto;
import com.example.springapptest.model.User;
import com.example.springapptest.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/getAllUser")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping("/register")
    public User register(@org.jetbrains.annotations.NotNull @RequestBody UserDto user){
        User createdUser=modelMapper.map(user,User.class);
        createdUser.setPassword(new BCryptPasswordEncoder().encode(createdUser.getPassword()));
        return userService.createUser(createdUser);
    }
}
