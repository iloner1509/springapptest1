package com.example.springapptest;

import com.example.springapptest.model.Role;
import com.example.springapptest.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void TestCreateFirstRole(){
        Role testRole=new Role("Admin","123");
        Role normalRole =new Role("Staff","test");
        List<Role> roleList=new ArrayList<Role>();
        roleList.add(testRole);
        roleList.add(normalRole);
        roleRepository.saveAll(roleList);
    }

}
