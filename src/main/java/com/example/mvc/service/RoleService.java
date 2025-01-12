package com.example.mvc.service;

import com.example.mvc.model.Role;
import com.example.mvc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    // Save multiple roles
    @Transactional
    public List<Role> saveAllRoles(List<Role> roles) {
        return roleRepository.saveAll(roles);
    }

    // Update a role
    @Transactional
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setTitle(roleDetails.getTitle());
        role.setDescription(roleDetails.getDescription());
        role.setDuties(roleDetails.getDuties());
        role.setIndustry(roleDetails.getIndustry());
        role.setName(roleDetails.getName());
        role.setContact(roleDetails.getContact());
        role.setLocation(roleDetails.getLocation());
        return roleRepository.save(role);
    }

    // Delete a role
    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    // Get roles based on title and industry filters
    public List<Role> findRolesByFilters(String title, String industry) {
        return roleRepository.findAll().stream()
                .filter(role -> (title == null || role.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (industry == null || role.getIndustry().toLowerCase().contains(industry.toLowerCase())))
                .collect(Collectors.toList());
    }
}
