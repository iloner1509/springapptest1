package com.example.springapptest.service;

import com.example.springapptest.model.Role;
import com.example.springapptest.repository.RoleRepository;

import java.util.List;

public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRole() {
//        return roleRepository.getListRoles();
        return null;
    }
}
