package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.Comment;
import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.CommentService;
import com.eventmanager.event_management.Service.EventService;
import com.eventmanager.event_management.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/events")
    public String getEvents(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        List<Event> getAllEvents = eventService.getAllEvents();
        model.addAttribute("getAllEvents", getAllEvents);
        return "events_list_page";
    }


    @GetMapping("/event/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getEventImage(@PathVariable("id") Long eventId) {
        Optional<Event> event = eventService.findById(eventId);

        if (eventId == 1) {
            return getLocalImage("event1.png");
        } else if (eventId == 2) {
            return getLocalImage("event2.gif");
        } else if (eventId == 3) {
            return getLocalImage("event3.jpg");
        } else if (eventId == 4) {
            return getLocalImage("event4.jpg");
        } else if (eventId == 5) {
            return getLocalImage("event5.jpg");
        }

        if (event.isPresent() && event.get().getImage() != null) {
            byte[] imageBytes = event.get().getImage();

            String contentType = "image/jpeg";

            if (isPng(imageBytes)) {
                contentType = "image/png";
            } else if (isGif(imageBytes)) {
                contentType = "image/gif";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private ResponseEntity<byte[]> getLocalImage(String filename) {
        try {
            File file = new File("src/main/resources/static/images/" + filename);
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            MediaType mediaType = getMediaTypeForFile(filename);
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType getMediaTypeForFile(String filename) {
        if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


    private boolean isPng(byte[] imageBytes) {
        return imageBytes.length > 8 && imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50
                && imageBytes[2] == (byte) 0x4E && imageBytes[3] == (byte) 0x47;
    }

    private boolean isGif(byte[] imageBytes) {
        return imageBytes.length > 6 && imageBytes[0] == 'G' && imageBytes[1] == 'I' && imageBytes[2] == 'F';
    }

    @GetMapping("/events/{id}/comments")
    public String getCommentsForEvent(@PathVariable("id") Long eventId, Model model, HttpSession session) {
        Optional<Event> event = eventService.findById(eventId);
        if (event.isEmpty()) {
            return "redirect:/events";
        }

        List<Comment> comments = commentService.getCommentsByEventId(eventId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        comments.forEach(comment -> comment.setFormattedDate(comment.getCreatedAt().format(formatter)));

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("event", event.get());
        model.addAttribute("comments", comments);
        model.addAttribute("loggedInUser", loggedInUser);

        return "comments_page";
    }

    @PostMapping("/events/{id}/comments")
    public String addComment(@PathVariable("id") Long eventId,
                             @RequestParam("commentText") String commentText,
                             HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Sprawdzanie, czy użytkownik jest zalogowany
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Sprawdzanie, czy wydarzenie istnieje
        Optional<Event> event = eventService.findById(eventId);
        if (event.isEmpty()) {
            return "redirect:/events";
        }

        // Tworzenie nowego komentarza
        Comment newComment = new Comment();
        newComment.setEvent(event.get());  // Ustaw ID eventu
        newComment.setUser(loggedInUser);  // Ustaw ID użytkownika
        newComment.setContent(commentText);  // Ustaw tekst komentarza
        newComment.setCreatedAt(LocalDateTime.now());  // Ustaw czas dodania komentarza

        // Zapisz komentarz w bazie
        commentService.save(newComment);

        // Przekierowanie po zapisaniu komentarza
        return "redirect:/events/" + eventId + "/comments";
    }
}