package com.example.springapptest.config;

import com.example.springapptest.common.Status;
import com.example.springapptest.model.Product;
import com.example.springapptest.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ProductConfig {
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {
            Product testProduct1=new Product(
                    "Test",
                    "image",
                    BigDecimal.valueOf(500000),
                    10,
                    "Pair",
                    Status.Active
            );
            productRepository.save(testProduct1);
        };
    }
}
