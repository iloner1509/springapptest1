package com.example.springapptest.controller;


import com.example.springapptest.dto.*;

import com.example.springapptest.model.CustomUserDetails;

import com.example.springapptest.model.Role;
import com.example.springapptest.model.User;
import com.example.springapptest.repository.RoleRepository;
import com.example.springapptest.repository.UserRepository;
import com.example.springapptest.security.JwtTokenProvider;

import javassist.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUser(){
            List<User> users = new ArrayList<User>();
            userRepository.findAll().forEach(users::add);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new NotFoundException("Cannot find user with id :"+id);
        }
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserDto user) {
            User createdUser = userRepository.save(
                    new User(user.getUsername(),
                            user.getEmail(),
                            passwordEncoder.encode(user.getPassword()),
                            user.getFullName(),
                            user.getAvatar(),
                            setRole(user.getRoles())));
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody UserDto user) throws NotFoundException {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User updatedUser = userData.get();
            updatedUser.setEmail(user.getEmail());
            updatedUser.setFullName(user.getFullName());
            updatedUser.setAvatar(user.getAvatar());
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            updatedUser.setStatus(user.getStatus());
            updatedUser.setRoles(setRole(user.getRoles()));
            return new ResponseEntity<>(userRepository.save(updatedUser),HttpStatus.OK);
        }
        else {
            throw new NotFoundException("Cannot update user with id"+id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") long id){
            userRepository.deleteById(id);
            return ResponseEntity.ok("Deleted user with "+id);
    }

    @DeleteMapping()
    public ResponseEntity deleteAllUser(){
            userRepository.deleteAll();
            return ResponseEntity.ok("All user has been deleted");
    }

    private Set<Role> setRole(Set<String> strRoles){
        Set<Role> roles=new HashSet<>();
        if (strRoles==null){
            Role userRole=roleRepository.findByName("User");
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role->{
                switch (role){
                    case "Admin":
                        Role adminRole=roleRepository.findByName("Admin");
                        roles.add(adminRole);
                        break;
                    case "Staff":
                        Role staffRole=roleRepository.findByName("Staff");
                        roles.add(staffRole);
                        break;
                    default:
                        Role userRole=roleRepository.findByName("User");
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }
}