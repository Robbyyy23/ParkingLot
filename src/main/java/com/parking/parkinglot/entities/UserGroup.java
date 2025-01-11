package com.parking.parkinglot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Username is mandatory")
    @Size(max = 100, message = "Username cannot exceed 100 characters")
    private String username;

    @Basic
    @Column(nullable = false, length = 50)
    @NotBlank(message = "User group is mandatory")
    @Size(max = 50, message = "User group name cannot exceed 50 characters")
    private String userGroup;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
