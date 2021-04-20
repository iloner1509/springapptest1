package com.example.springapptest;

import com.example.springapptest.model.Category;
import com.example.springapptest.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void TestCreateCategory(){

        Category testCategory1=new Category("Air plane","test");
        Category testCategory2=new Category("Jet fighter","test",testCategory1);
        Category testCategory3=new Category("Helicopter","test",testCategory1);

        List<Category> categoryList= Arrays.asList(testCategory1,testCategory2,testCategory3);
        categoryRepository.saveAll(categoryList);
    }
}
