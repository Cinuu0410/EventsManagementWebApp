package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.AdminService;
import com.eventmanager.event_management.Service.SliderImageService;
import com.eventmanager.event_management.Service.UserService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private UserService userService;
    @Autowired
    private SliderImageService sliderImageService;

    @PostMapping("/add-event")
    public String addEvent(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image,
            @RequestParam("eventDate") String eventDate,
            HttpSession session) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime eventDateTime = LocalDateTime.parse(eventDate, formatter);

            adminService.saveEvent(title, description, category, image, eventDateTime);

            User loggedInUser = (User) session.getAttribute("loggedInUser");

            if (loggedInUser != null) {
                String role = loggedInUser.getRole();
                if ("Administrator".equalsIgnoreCase(role)) {
                    return "redirect:/admin_panel";
                } else if ("Moderator".equalsIgnoreCase(role)) {
                    return "redirect:/mod_panel";
                }
            }
            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
            return "error_page";
        }
    }

    @PostMapping("/add-image")
    public String addImage(@RequestParam("image") MultipartFile image, HttpSession session){
        try {
            sliderImageService.saveImage(image);

            User loggedInUser = (User) session.getAttribute("loggedInUser");

            if (loggedInUser != null) {
                String role = loggedInUser.getRole();
                if ("Administrator".equalsIgnoreCase(role)) {
                    return "redirect:/admin_panel";
                } else if ("Moderator".equalsIgnoreCase(role)) {
                    return "redirect:/mod_panel";
                }
            }
            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
            return "error_page";
        }
    }


}
