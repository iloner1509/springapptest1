package com.example.springapptest.service;

import com.example.springapptest.model.Product;
import com.example.springapptest.model.User;

import java.util.List;

public interface IUserService  {
    List<User> getAllUser();
    User saveUser(User user);
    User getUserById(String id);
    User getUserByName(String name);
    void deleteUser(String id);
    User createUser(User user);
}
