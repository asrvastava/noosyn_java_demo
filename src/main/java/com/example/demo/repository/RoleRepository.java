package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Role;
import java.util.List;

/**
 * Repository interface for accessing Role entities.
 * extends JpaRepository to provide standard CRUD operations.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds roles associated with a specific username.
     *
     * @param username the username to search for roles
     * @return a list of roles associated with the given username
     */
    List<Role> findByUserUsername(String username);
}
