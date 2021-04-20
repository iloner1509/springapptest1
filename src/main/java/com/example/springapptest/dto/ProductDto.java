package com.example.springapptest.dto;

import com.example.springapptest.common.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {

    public String name;

    public String image;

    private BigDecimal price;

    private Integer quantity;

    private String unit;

    private Status status;

    private Integer category;
}
