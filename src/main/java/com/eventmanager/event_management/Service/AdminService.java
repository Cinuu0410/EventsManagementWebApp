package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AdminService {

    @Autowired
    private EventRepository eventRepository;

    public void saveEvent(String title, String description, MultipartFile image, LocalDateTime eventDate) throws IOException {
        byte[] imageBytes = image.getBytes();

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setImage(imageBytes);
        event.setEventDate(eventDate);
        event.setCreatedAt(LocalDateTime.now());

        eventRepository.save(event);
    }
}
