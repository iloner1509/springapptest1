package com.example.springapptest.controller;


import com.example.springapptest.dto.*;

import com.example.springapptest.model.CustomUserDetails;

import com.example.springapptest.model.Role;
import com.example.springapptest.model.User;
import com.example.springapptest.repository.RoleRepository;
import com.example.springapptest.repository.UserRepository;
import com.example.springapptest.security.JwtTokenProvider;

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


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          JwtTokenProvider jwtTokenProvider,
                          BCryptPasswordEncoder passwordEncoder,
                          ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginDto loginRequest) {
        return getResponseEntity(loginRequest, authenticationManager, jwtTokenProvider);
    }

    @NotNull
    static ResponseEntity<?> getResponseEntity(@RequestBody @Validated LoginDto loginRequest,
                                               AuthenticationManager authenticationManager,
                                               JwtTokenProvider jwtTokenProvider) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUser() {
        try {
            List<User> users = new ArrayList<User>();
            userRepository.findAll().forEach(users::add);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserDto user) {
        try {
            User createdUser = userRepository.save(
                    new User(user.getUsername(),
                            user.getEmail(),
                            passwordEncoder.encode(user.getPassword()),
                            user.getFullName(),
                            user.getAvatar(),
                            setRole(user.getRoles())));
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody UserDto user) {
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") long id){
        try{
            userRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Đã xóa user "+id));
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping()
    public ResponseEntity deleteAllUser(){
        try{
            userRepository.deleteAll();
            return ResponseEntity.ok(new MessageResponse("Đã xóa toàn bộ user"));
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody RegisterDto registerRequest){
        try{
            if (userRepository.existsByUsername(registerRequest.getUsername())){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Tên tài khoản đã được sử dụng!"));
            }

            User user=new User(registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    registerRequest.getUsername());
            user.setRoles(setRole(registerRequest.getRoles()));
            userRepository.save(user);
            return new ResponseEntity(user,HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

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