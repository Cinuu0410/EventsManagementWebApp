package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/add-event")
    public String addEvent(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam("eventDate") String eventDate) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime eventDateTime = LocalDateTime.parse(eventDate, formatter);

            adminService.saveEvent(title, description, image, eventDateTime);

            return "redirect:/admin_panel";
        } catch (IOException e) {
            e.printStackTrace();
            return "error_page";
        }
    }
}
