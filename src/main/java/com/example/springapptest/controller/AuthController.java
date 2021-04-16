package com.example.springapptest.controller;

import com.example.springapptest.dto.JwtResponse;
import com.example.springapptest.model.CustomUserDetails;
import com.example.springapptest.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginDto loginRequest) {
//        return getResponseEntity(loginRequest, authenticationManager, jwtTokenProvider);
//    }
    @GetMapping("/refreshtoken")
    public ResponseEntity refreshToken(HttpServletRequest request) {
        CustomUserDetails userPrincipal = (CustomUserDetails) request.getUserPrincipal();
        String token = jwtTokenProvider.doGenerateRefreshToken(userPrincipal.getUsername());

        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(
                new JwtResponse(token, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getEmail(), roles));
    }
}
