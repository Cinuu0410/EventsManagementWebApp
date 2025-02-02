package com.eventmanager.event_management.Repository;

import com.eventmanager.event_management.Model.CommentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRatingRepository extends JpaRepository<CommentRating, Long> {

    List<CommentRating> findByCommentId(Long commentId);

    Optional<CommentRating> findByCommentIdAndUserId(Long commentId, Long userId);
}

