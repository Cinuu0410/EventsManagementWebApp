package com.eventmanager.event_management.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private LocalDateTime createdAt;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Setter
    @Getter
    @Transient
    private String formattedDate;

    private Double rating;

    public void setRating(Double rating) {
        this.rating = rating != null ? Math.round(rating * 10.0) / 10.0 : null;
    }
}
