package com.example.shortener.repository.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "user")
@Entity
public class UserEntity {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

}
