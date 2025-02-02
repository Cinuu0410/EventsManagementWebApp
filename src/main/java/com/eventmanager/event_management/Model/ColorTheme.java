package com.eventmanager.event_management.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "color_themes")
public class ColorTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String primaryColor;
    private String secondaryColor;
    private String buttonColor;
}
