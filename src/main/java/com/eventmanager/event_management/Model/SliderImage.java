package com.eventmanager.event_management.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="SliderImage")
public class SliderImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;

    private boolean inSlider;

    @Transient
    private int position;

    @Column(name = "order_index")
    private int orderIndex;

    private String description;
}
