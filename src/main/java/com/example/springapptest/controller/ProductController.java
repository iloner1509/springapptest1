package com.example.springapptest.controller;


import com.example.springapptest.dto.ProductDto;
import com.example.springapptest.model.Product;
import com.example.springapptest.repository.CategoryRepository;
import com.example.springapptest.repository.ProductRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/product")
public class ProductController {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {

        List<Product> products = new ArrayList<Product>();
        productRepository.findAll().forEach(products::add);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            throw new NotFoundException("Cannot find product with id :" + id);
        }
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto product) {

        Product createdProduct = productRepository.save(new Product(
                product.getName(),
                product.getImage(),
                product.getPrice(),
                product.getQuantity(),
                product.getUnit(),
                categoryRepository.findById(product.getCategory()).get()));

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody ProductDto product) throws NotFoundException {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            Product updatedProduct = productData.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setImage(product.getImage());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setUnit(product.getUnit());
            updatedProduct.setQuantity(product.getQuantity());
            updatedProduct.setStatus(product.getStatus());

            return new ResponseEntity<>(productRepository.save(updatedProduct),HttpStatus.OK);
        }
        else {
            throw new NotFoundException("Cannot update user with id"+id);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Deleted product with " + id);
    }

    @DeleteMapping()
    public ResponseEntity deleteAllProduct(){
        productRepository.deleteAll();
        return ResponseEntity.ok("All product has been deleted");
    }
}
