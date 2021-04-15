package com.example.springapptest.controller;


import com.example.springapptest.dto.JwtResponse;
import com.example.springapptest.dto.LoginDto;

import com.example.springapptest.dto.MessageResponse;
import com.example.springapptest.dto.RegisterDto;
import com.example.springapptest.model.CustomUserDetails;

import com.example.springapptest.model.Role;
import com.example.springapptest.model.User;
import com.example.springapptest.repository.RoleRepository;
import com.example.springapptest.repository.UserRepository;
import com.example.springapptest.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/user/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
//
//
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(){
        try {
            List<User> users=new ArrayList<User>();
            userRepository.findAll().forEach(users::add);
            if (users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users,HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//
    @PostMapping("/user/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterDto registerRequest){
        if (userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Tên tài khoản đã được sử dụng!"));
        }

        User user=new User(registerRequest.getEmail(),passwordEncoder.encode(registerRequest.getPassword()),registerRequest.getUsername());
        Set<String> strRoles=registerRequest.getRoles();
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
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Đăng ký thành công"));
    }




}
