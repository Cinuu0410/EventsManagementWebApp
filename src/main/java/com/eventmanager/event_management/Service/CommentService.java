package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.Comment;
import com.eventmanager.event_management.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;


    public List<Comment> getCommentsByEventId(Long eventId) {
        return commentRepository.findByEventId(eventId);
    }

    public List<Comment> getAcceptedCommentsByEventId(Long eventId) {
        return commentRepository.findAcceptedCommentsByEventId(eventId);
    }

    public List<Comment> getPendingComments() {
        return commentRepository.findByIsApprovedFalse();
    }

    public Optional<Comment> getById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
