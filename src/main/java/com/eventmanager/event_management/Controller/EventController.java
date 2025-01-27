package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.CartItem;
import com.eventmanager.event_management.Model.Comment;
import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.CommentService;
import com.eventmanager.event_management.Service.EventService;
import com.eventmanager.event_management.Service.UserService;
import jakarta.servlet.http.HttpSession;
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
        for (Event event : getAllEvents) {
            switch (event.getCategory()) {
                case "concert":
                    event.setCategory("Koncert");
                    break;
                case "workshops":
                    event.setCategory("Warsztaty");
                    break;
                case "conference":
                    event.setCategory("Konferencja");
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("getAllEvents", getAllEvents);
        addCartTotalToModel(session, model);
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
            return getLocalImage("event4.png");
        } else if (eventId == 5) {
            return getLocalImage("event5.gif");
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

        List<Comment> comments = commentService.getAcceptedCommentsByEventId(eventId);

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

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Optional<Event> event = eventService.findById(eventId);
        if (event.isEmpty()) {
            return "redirect:/events";
        }

        Comment newComment = new Comment();
        newComment.setEvent(event.get());
        newComment.setUser(loggedInUser);
        newComment.setContent(commentText);
        newComment.setCreatedAt(LocalDateTime.now());
        String role = loggedInUser.getRole();
        newComment.setApproved("Administrator".equalsIgnoreCase(role) || "Moderator".equalsIgnoreCase(role));
        commentService.save(newComment);

        return "redirect:/events/" + eventId + "/comments";
    }

    @GetMapping("/admin/approve-comments")
    public String getPendingComments(Model model) {
        // Pobierz komentarze oczekujące na akceptację (isApproved == false)
        List<Comment> pendingComments = commentService.getPendingComments();

        // Dodaj komentarze do modelu
        model.addAttribute("comments", pendingComments);

        return "waiting_comments_page";  // Strona oczekujących komentarzy
    }

    @PostMapping("/admin/comments/accept/{id}")
    public String acceptComment(@PathVariable("id") Long commentId) {
        // Pobierz komentarz po ID
        Optional<Comment> commentOptional = commentService.getById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            // Ustaw isApproved na true
            comment.setApproved(true);
            // Zapisz zaktualizowany komentarz
            commentService.save(comment);
        }

        // Po akceptacji przekieruj na stronę z oczekującymi komentarzami
        return "redirect:/admin/comments/approve-comments";
    }

    @GetMapping("/events/{id}/order")
    public String orderTicketPage(@PathVariable Long id, Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }else {
            return "redirect:/login";
        }

        List<Event> getAllEvents = eventService.getAllEvents();
        for (Event event : getAllEvents) {
            switch (event.getCategory()) {
                case "concert":
                    event.setCategory("Koncert");
                    break;
                case "workshops":
                    event.setCategory("Warsztaty");
                    break;
                case "conference":
                    event.setCategory("Konferencja");
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("getAllEvents", getAllEvents);

        Event event = eventService.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wydarzenia o ID: " + id));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'o' HH:mm");
        String formattedDate = event.getEventDate().format(formatter);

        addCartTotalToModel(session, model);

        model.addAttribute("event", event);
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("quantity", 1);
        return "order_page";
    }

    protected void addCartTotalToModel(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        double totalAmount = 0.0;

        if (cart != null) {
            for (CartItem item : cart) {
                totalAmount += item.getTotalPrice();
            }
        }
        model.addAttribute("totalAmount", totalAmount);
    }
}