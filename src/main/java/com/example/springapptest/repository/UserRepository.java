package com.example.springapptest.repository;

import com.example.springapptest.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    //    Optional<User> findUserByEmail(String email);
    User findByUsername(String username);

    Boolean existsByUsername(String username);

}
