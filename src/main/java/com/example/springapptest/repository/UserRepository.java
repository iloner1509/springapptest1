package com.example.springapptest.repository;

import com.example.springapptest.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    //    Optional<User> findUserByEmail(String email);
    User findByUserName(String username);
}
