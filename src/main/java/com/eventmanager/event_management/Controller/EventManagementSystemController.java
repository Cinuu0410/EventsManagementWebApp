package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.SliderImage;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class EventManagementSystemController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private SliderImageService sliderImageService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ModeratorService moderatorService;

    @GetMapping("/main_page")
    public String mainPage(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);

            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        return "main_page";
    }

    @GetMapping("/eventsList")
    public String eventsList(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        return "redirect:/events";
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        return "contact_page";
    }

    @GetMapping("/slider/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getSliderImage(@PathVariable Long id) {
        Optional<SliderImage> sliderImage = sliderImageService.findById(id);

        if (sliderImage.isPresent() && sliderImage.get().getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Domyślnie zakładam format JPEG
                    .body(sliderImage.get().getImage());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/photo_gallery")
    public String aboutUsPage(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        // Pobierz zdjęcia, które mają ustawiony inSlider na true
        List<SliderImage> getSliderImages = sliderImageService.getSliderImages();
        model.addAttribute("getSliderImages", getSliderImages);
        return "photo_gallery_page";
    }

    @GetMapping("/admin_panel")
    public String adminPanelPage(Model model, HttpSession session) {
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
        return "admin_panel_page";
    }

    @GetMapping("/admin/upload-images")
    public String uploadImagesPage(Model model, HttpSession session) {
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
        return "create_new_event_page";
    }

    @GetMapping("/admin/upload-images-to-database")
    public String uploadImagesToDatabase(Model model, HttpSession session) {
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
        return "upload_images_to_database_page";
    }

    @GetMapping("/admin/edit-slider")
    public String editSliderPage(Model model, HttpSession session) {
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
        List<SliderImage> allImages = sliderImageService.getAllSliderImages();
        model.addAttribute("allImages", allImages);
        return "edit_slider_page";
    }

    @PostMapping("/slider/update")
    public String updateSliderImages(@RequestParam(value = "selectedImages", required = false) List<Long> selectedImages) {
        System.out.println("Wybrane zdjęcia: " + selectedImages);
        sliderImageService.updateSliderImages(selectedImages);
        return "redirect:/admin_panel";
    }

    @GetMapping("/admin/set-category-to-mod")
    public String setCategoryToMod(Model model, HttpSession session) {
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

        List<User> getModerators = userService.getModerators();
        model.addAttribute("getModerators", getModerators);


        return "set_category_to_mod_page";
    }

    @PostMapping("/admin/set-category-to-mod")
    public String setCategoryToMod(@RequestParam("moderatorId") Long moderatorId,
                                   @RequestParam("category") String category) {

        // Znajdź moderatora
        User moderator = userService.getUserById(moderatorId);

        if (moderator != null) {
            // Przypisz kategorię
            moderatorService.assignCategoryToModerator(moderatorId, category);
            return "redirect:/admin/set-category-to-mod";
        } else {
            // Jeśli nie znaleziono użytkownika, wyświetl błąd
            return "redirect:/admin/set-category-to-mod?error=true";
        }
    }
}
