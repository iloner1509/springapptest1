package com.example.springapptest.dto;

import com.example.springapptest.common.Status;
import com.example.springapptest.model.Category;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

public class ProductDto {

    public String name;

    public String image;

    private BigDecimal price;

    private Integer quantity;

    private String unit;

    private Status status;

    private Integer category;
}
