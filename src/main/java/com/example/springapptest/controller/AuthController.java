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
import io.jsonwebtoken.impl.DefaultClaims;
import org.jetbrains.annotations.NotNull;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          RoleRepository roleRepository,
                          UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @GetMapping("/refreshtoken")
    public ResponseEntity refreshToken(HttpServletRequest request) {
//
        DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromJsonwebtokenClaims(claims);


        String token = jwtTokenProvider.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        User currentUser = userRepository.findByUsername(expectedMap.get("sub").toString());

        List<String> roles = currentUser.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(token, currentUser.getId(), currentUser.getUsername(), currentUser.getEmail(), roles));
    }

    private Map<String, Object> getMapFromJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody RegisterDto registerRequest) {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Tên tài khoản đã được sử dụng!"));
            }

            User user = new User(registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    registerRequest.getUsername());
            user.setRoles(setRole(registerRequest.getRoles()));
            userRepository.save(user);
            return new ResponseEntity(user, HttpStatus.CREATED);


    }

    private Set<Role> setRole(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName("User");
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "Admin":
                        Role adminRole = roleRepository.findByName("Admin");
                        roles.add(adminRole);
                        break;
                    case "Staff":
                        Role staffRole = roleRepository.findByName("Staff");
                        roles.add(staffRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName("User");
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }

}
