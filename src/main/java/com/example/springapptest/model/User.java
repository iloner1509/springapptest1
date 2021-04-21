package com.example.springapptest.model;

import com.example.springapptest.common.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    private String avatar;

    private Status status=Status.Active;

    public User(String email,
                String password,
                String fullName,
                String avatar,
                Status status,
                String username) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.status = status;
        this.username=username;
    }
    public User(String email,
                String password,
                String username) {
        this.email = email;
        this.password = password;
        this.username=username;
    }

    public User(String username,
                String email,
                String password,
                String fullName,
                String avatar,
                Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles=new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Order> orders=new HashSet<>();

    public void addRole(Role role){
        this.roles.add(role);
    }
}
