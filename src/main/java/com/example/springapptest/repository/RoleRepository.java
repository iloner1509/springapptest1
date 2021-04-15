package com.example.springapptest.repository;

import com.example.springapptest.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    //    @Value(value = "select * from roles")
//    public List<Role> getListRoles();
    Role findByName(String name);
}

