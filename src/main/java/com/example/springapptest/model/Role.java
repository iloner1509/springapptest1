package com.example.springapptest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
//    @Column(name = "id", updatable = false, nullable = false)
//    private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150,nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users=new ArrayList<>();

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

//    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
//    @JoinTable(name = "role_permission",joinColumns = {
//            @JoinColumn(name = "role_id")
//    },inverseJoinColumns = {
//            @JoinColumn(name = "permission_id")
//    })
//    private Set<Permission> permissions=new HashSet<>();

}
