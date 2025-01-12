package com.example.mvc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String duties;
    private String name;
    private String contact;
    private String location;
    private String industry;

    private String Dateposted;

    // Default constructor
    public Role() {}

    // All-Args Constructor
    public Role(Long id, String title, String description, String duties, String name, String contact, String location, String industry) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duties = duties;
        this.name = name;
        this.contact = contact;
        this.location = location;
        this.industry = industry;
        this.Dateposted=Dateposted;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateposted() {
        return Dateposted;
    }

    public void setDateposted(String dateposted) {
        Dateposted = dateposted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
