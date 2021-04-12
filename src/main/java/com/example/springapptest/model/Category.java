package com.example.springapptest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Category(String name, String description, Category parent, Set<Category> child) {
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.child = child;
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> child = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
