package com.eventmanager.event_management.Repository;

import com.eventmanager.event_management.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEventId(Long eventId);

    @Query("SELECT c FROM Comment c WHERE c.event.id = :eventId AND c.isApproved = true")
    List<Comment> findAcceptedCommentsByEventId(@Param("eventId") Long eventId);

    @Query("SELECT c FROM Comment c WHERE c.isApproved = false")
    List<Comment> findByIsApprovedFalse();
}
