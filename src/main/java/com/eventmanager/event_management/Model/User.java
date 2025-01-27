package com.eventmanager.event_management.Model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String assignedCategory;
    private String role;


    @Transient
    private String rawPassword;

}
