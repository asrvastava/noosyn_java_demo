package com.example.demo.service;
import com.example.demo.exception.AppException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;

    public void createRole(String roleName ,String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new AppException("OD-02"));
        
        Role role = new Role();
        role.setRoleName(roleName);
        role.setUser(user);
        roleRepository.save(role);
    }

    public String getRoleByUsername(String username) {
        return roleRepository.findByUserUsername(username)
                .map(Role::getRoleName)
                .orElseThrow(() -> new AppException("OD-04"));
    }
}
