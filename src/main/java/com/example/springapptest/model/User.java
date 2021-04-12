package com.example.springapptest.model;

import com.example.springapptest.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(length = 100,nullable = false,unique = true)
    private String email;

    @Column(length = 50,nullable = false)
    private String password;

    @Column(name = "full_name",length = 50)
    private String fullName;

    @Column(length = 100)
    private String Avatar;

    private Status status;

    public User(String email,
                String password,
                String fullName,
                String avatar,
                Status status) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        Avatar = avatar;
        this.status = Status.Active;
    }
}
