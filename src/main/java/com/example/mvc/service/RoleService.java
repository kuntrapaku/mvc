package com.example.mvc.service;

import com.example.mvc.model.Role;
import com.example.mvc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Get all roles
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }


    // Save a single role
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    // Save multiple roles
    public List<Role> saveAllRoles(List<Role> roles) {
        return roleRepository.saveAll(roles);
    }

    // Update a role
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setTitle(roleDetails.getTitle());
        role.setDescription(roleDetails.getDescription());
        return roleRepository.save(role);
    }

    // Delete a role
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
    public List<Role> findRolesByFilters(String title, String industry) {
        return roleRepository.findAll().stream()
                .filter(role -> (title == null || role.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (industry == null || role.getIndustry().toLowerCase().contains(industry.toLowerCase())))
                .collect(Collectors.toList());
    }


    // Partially update a role
    public Role partiallyUpdateRole(Long id, Map<String, String> updates) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        updates.forEach((field, value) -> {
            switch (field) {
                case "title":
                    role.setTitle(value);
                    break;
                case "description":
                    role.setDescription(value);
                    break;
                default:
                    break;
            }
        });
        return roleRepository.save(role);
    }
}
