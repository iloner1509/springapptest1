package com.example.springapptest.controller;

import com.example.springapptest.dto.CategoryDto;
import com.example.springapptest.model.Category;
import com.example.springapptest.repository.CategoryRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Integer id) throws NotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        } else {
            throw new NotFoundException("Cannot find category with id :" + id);
        }
    }

    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto category) throws NotFoundException {
        Category createdCategory;
        if (category.getParentId() != null) {
            Optional<Category> parentCategory = categoryRepository.findById(category.getParentId());
            if (parentCategory.isPresent()) {
                createdCategory = categoryRepository.save(
                        new Category(category.getName(),
                                category.getDescription(),
                                parentCategory.get()));
            } else {
                throw new NotFoundException("Cannot category with id :" + category.getParentId() + " not exist");
            }
        } else {
            createdCategory = categoryRepository.save(
                    new Category(category.getName(),
                            category.getDescription()));
        }

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    //    @PutMapping("/{id}")
//    public ResponseEntity<Category> updateCategory(@PathVariable("id") Integer id, @RequestBody CategoryDto category) throws NotFoundException {
//        Optional<Category> categoryData = categoryRepository.findById(id);
//        if (categoryData.isPresent()) {
//            Category updatedCategory = categoryData.get();
//            updatedCategory.setName(category.getName());
//            updatedCategory.setName(category.getDescription());
//            updatedCategory.setParent(categoryRepository.findById(category.getParentId()).get());
//            updatedCategory.setChild();
//            updatedUser.setFullName(user.getFullName());
//            updatedUser.setAvatar(user.getAvatar());
//            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
//            updatedUser.setStatus(user.getStatus());
//            updatedUser.setRoles(setRole(user.getRoles()));
//            return new ResponseEntity<>(userRepository.save(updatedUser),HttpStatus.OK);
//        }
//        else {
//            throw new NotFoundException("Cannot update user with id"+id);
//        }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategoryById(@PathVariable("id") Integer id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Deleted category with " + id);
    }

    @DeleteMapping()
    public ResponseEntity deleteAllCategory(){
//        categoryRepository.updateParentIdToNull();
//        categoryRepository.deleteAllCategory();
        categoryRepository.deleteAll();
        return ResponseEntity.ok("All category has been deleted");
    }
}
