package com.example.springapptest;

import com.example.springapptest.model.Role;
import com.example.springapptest.repository.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class PermissonRepositoryTests {

    @Autowired
    private PermissionRepository permissionRepository;

//    @Test
//    public void TestCreatePermission(){
//        Role testRole=new Role("Admin","123");
//        Role normalRole =new Role("Staff","test");
//        Role userRole =new Role("User","user role");
//        List<Role> roleList=new ArrayList<Role>();
//        roleList.add(testRole);
//        roleList.add(normalRole);
//        roleList.add(userRole);
//        roleRepository.saveAll(roleList);
//    }
}
