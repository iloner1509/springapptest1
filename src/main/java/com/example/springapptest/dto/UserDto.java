package com.example.springapptest.dto;

import com.example.springapptest.common.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String username;

    private String email;

    private String password;

    private String fullName;

    private String avatar;

    private Status status;

    private Set<String> roles;
}
