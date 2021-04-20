package com.example.springapptest;

import com.example.springapptest.model.Category;
import com.example.springapptest.model.Product;
import com.example.springapptest.model.Role;
import com.example.springapptest.repository.CategoryRepository;
import com.example.springapptest.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void TestCreateProduct(){
        Product testProduct1=new Product("Test product 1","test", BigDecimal.valueOf(50000000),15,"piece",categoryRepository.findByName("Jet fighter"));
        Product testProduct2=new Product("Test product 2","test 2", BigDecimal.valueOf(170000000),9,"pair",categoryRepository.findByName("Air plane"));
        Product testProduct3=new Product("Test product 3","test 3", BigDecimal.valueOf(80000000),15,"can",categoryRepository.findByName("Helicopter"));

        List<Product> productList= Arrays.asList(testProduct1,testProduct2,testProduct3);
        productRepository.saveAll(productList);
    }
}
