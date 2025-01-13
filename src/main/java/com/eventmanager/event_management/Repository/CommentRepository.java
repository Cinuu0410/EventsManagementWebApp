package com.eventmanager.event_management.Repository;

import com.eventmanager.event_management.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEventId(Long eventId);
}
