package com.eventmanager.event_management.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comment_rating")
public class CommentRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer rating;
}
