package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.User;

/**
 * Repository interface for accessing User entities.
 * extends JpaRepository to provide standard CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
