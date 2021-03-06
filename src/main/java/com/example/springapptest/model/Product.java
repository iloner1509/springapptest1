package com.example.springapptest.model;


import com.example.springapptest.common.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "products")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    public String name;

    public String image;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal price = BigDecimal.valueOf(0);

    @Column(length = 50)
    private Integer quantity = 0;

    @Column(length = 50)
    private String unit;

    @Column(nullable = false)
    private Status status = Status.InActive;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name,
                   String image,
                   BigDecimal price,
                   Integer quantity,
                   String unit,
                   Category category
    ) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
    }

    @OneToMany(mappedBy = "product",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<OderDetail> orderDetail=new HashSet<>();

}
