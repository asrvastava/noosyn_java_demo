package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUserUsername(String username);
}
