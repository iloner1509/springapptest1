package com.example.springapptest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class CategoryDto {

    private String name;

    private String description;

    private Integer parentId;

}
