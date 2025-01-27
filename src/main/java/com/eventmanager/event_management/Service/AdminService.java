package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private EventRepository eventRepository;

    public void saveEvent(String title, String description, String category, MultipartFile image, LocalDateTime eventDate, Double price) throws IOException {
        byte[] imageBytes = image.getBytes();

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setCategory(category);
        event.setImage(imageBytes);
        event.setEventDate(eventDate);
        event.setCreatedAt(LocalDateTime.now());
        event.setPrice(price);

        eventRepository.save(event);
    }


    public void editEvent(Long eventId, String title, String description, String category,
                          MultipartFile image, LocalDateTime eventDate, double price) throws IOException {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setTitle(title);
            event.setDescription(description);
            event.setCategory(category);
            event.setEventDate(eventDate);
            event.setPrice(price);

            // Jeśli przesłano nowe zdjęcie
            if (image != null && !image.isEmpty()) {
                byte[] imageData = image.getBytes();
                event.setImage(imageData); // Zakładam, że `Event` ma pole `image` typu byte[]
            }

            eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("Wydarzenie o podanym ID nie istnieje");
        }
    }
}
