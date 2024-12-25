package com.example.mvc.controller;

import com.example.mvc.model.Role;
import com.example.mvc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5000")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Get all roles
    @GetMapping("/roles")
    public List<Role> getFilteredRoles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String industry
    ) {
        System.out.println("Title: " + title + ", Industry: " + industry);
        List<Role> filteredRoles = roleService.findRolesByFilters(title, industry);
        System.out.println("Filtered Roles: " + filteredRoles);
        return filteredRoles;
    }


    // Create a single role
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    // Create multiple roles
    @PostMapping("/batch")
    public ResponseEntity<List<Role>> createRoles(@RequestBody List<Role> roles) {
        List<Role> savedRoles = roleService.saveAllRoles(roles);
        return ResponseEntity.ok(savedRoles);
    }

    // Update a role
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Role updatedRole = roleService.updateRole(id, roleDetails);
        return ResponseEntity.ok(updatedRole);
    }

    // Delete a role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    // Partially update a role
    @PatchMapping("/{id}")
    public ResponseEntity<Role> partiallyUpdateRole(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Role role = roleService.partiallyUpdateRole(id, updates);
        return ResponseEntity.ok(role);
    }

    // Load roles from JSON file
    @PostMapping("/load")
    public ResponseEntity<List<Role>> loadRolesFromJson() {
        try {
            // Locate and read the JSON file from resources
            ClassPathResource resource = new ClassPathResource("roles.json");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Role> roles = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Role>>() {});

            // Save roles to the database
            roles.forEach(roleService::saveRole);

            return ResponseEntity.ok(roles);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
