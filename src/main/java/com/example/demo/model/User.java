package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a user.
 * Maps to the "users" table in the database.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
}
