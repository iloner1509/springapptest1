package com.example.springapptest;

import com.example.springapptest.common.Status;
import com.example.springapptest.model.Role;
import com.example.springapptest.model.User;
import com.example.springapptest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {


    private UserRepository userRepository;
    private EntityManager entityManager;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository, EntityManager entityManager) {
        super();
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Test
    public void TestCreateUser() {
        User admin = new User("trung@gmail.com", "123456", "trung", "avatar", Status.Active);
        User staff = new User("test@gmail", "123456", "trung", "avatar", Status.Active);

        Role roleAdmin = entityManager.createQuery(
                "SELECT u from Role u WHERE u.name = :role", Role.class).setParameter("role", "Admin").getSingleResult();
        Role roleStaff = entityManager.createQuery(
                "SELECT u from Role u WHERE u.name = :role", Role.class).setParameter("role", "Staff").getSingleResult();
//        System.out.println(roleStaff.getName());
        admin.addRole(roleAdmin);
        staff.addRole(roleStaff);

        List<User> userList = new ArrayList<User>();
        userList.add(admin);
        userList.add(staff);
        userRepository.saveAll(userList);

    }
}
