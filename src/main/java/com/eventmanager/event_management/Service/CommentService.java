package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.Comment;
import com.eventmanager.event_management.Model.CommentRating;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Repository.CommentRatingRepository;
import com.eventmanager.event_management.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentRatingRepository commentRatingRepository;

    @Autowired
    private UserService userService;


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

    public void rateComment(Long commentId, Long userId, Integer rating) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            Optional<CommentRating> existingRating = commentRatingRepository.findByCommentIdAndUserId(commentId, userId);
            if (existingRating.isPresent()) {
                CommentRating ratingToUpdate = existingRating.get();
                ratingToUpdate.setRating(rating);
                commentRatingRepository.save(ratingToUpdate);
            } else {
                CommentRating newRating = new CommentRating();
                newRating.setComment(comment);
                User user = userService.getUserById(userId);
                if (user != null) {
                    newRating.setUser(user);
                } else {
                    throw new RuntimeException("User not found with ID: " + userId);
                }
                newRating.setRating(rating);
                commentRatingRepository.save(newRating);
            }
        }
    }

    public Double getAverageRatingForComment(Long commentId) {
        List<CommentRating> ratings = commentRatingRepository.findByCommentId(commentId);
        if (ratings.isEmpty()) {
            return null;
        }
        double average = ratings.stream()
                .mapToDouble(CommentRating::getRating)
                .average()
                .orElse(0.0);

        return Math.round(average * 10.0) / 10.0;
    }
}
