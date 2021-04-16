package com.example.springapptest.service;

import com.example.springapptest.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProduct();

    Product getProductById(Long id);

}
