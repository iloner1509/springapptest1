package com.example.springapptest.service;

import com.example.springapptest.model.Product;

import java.util.List;

public interface IProductService {
    public List<Product> getAllProduct();
    public Product getProductById(Long id);

}
