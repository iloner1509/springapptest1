package com.example.springapptest.service;

import com.example.springapptest.model.Product;
import com.example.springapptest.model.User;

import java.util.List;

public interface IUserService  {
    public List<User> getAllUser();
    public User saveUser(User user);
    public User getUserById(String id);
    public User getUserByName(String name);
    public String deleteUser(String id);
    public Product updateProduct(Product product);

}
