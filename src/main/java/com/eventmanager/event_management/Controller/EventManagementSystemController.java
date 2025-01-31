package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.*;
import com.eventmanager.event_management.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private OrderService orderService;

    @GetMapping("/main_page")
    public String mainPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }

        addCartTotalToModel(session, model);

        return "main_page";
    }

    @GetMapping("/eventsList")
    public String eventsList(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        addCartTotalToModel(session, model);

        return "redirect:/events";
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpSession session) {
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
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(sliderImage.get().getImage());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/photo_gallery")
    public String aboutUsPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        }
        List<SliderImage> getSliderImages = sliderImageService.getSliderImages();
        model.addAttribute("getSliderImages", getSliderImages);
        addCartTotalToModel(session, model);
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
        } else {
            return "redirect:/login";
        }
        return "admin_panel_page";
    }

    @GetMapping("/admin/create-event-page")
    public String uploadImagesPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        } else {
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
        } else {
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
        } else {
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
        } else {
            return "redirect:/login";
        }

        List<User> getModerators = userService.getModerators();
        model.addAttribute("getModerators", getModerators);


        return "set_category_to_mod_page";
    }

    @PostMapping("/admin/set-category-to-mod")
    public String setCategoryToMod(@RequestParam("moderatorId") Long moderatorId,
                                   @RequestParam("category") String category) {

        User moderator = userService.getUserById(moderatorId);

        if (moderator != null) {
            moderatorService.assignCategoryToModerator(moderatorId, category);
            return "redirect:/admin/set-category-to-mod";
        } else {
            return "redirect:/admin/set-category-to-mod?error=true";
        }
    }

    @GetMapping("/mod_panel")
    public String modPanelPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        } else {
            return "redirect:/login";
        }
        return "mod_panel_page";
    }

    @GetMapping("/admin/edit-event/{id}")
    public String editEventsPage(@PathVariable("id") Long eventId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
        } else {
            return "redirect:/login";
        }

        Optional<Event> event = eventService.findById(eventId);
        if (event.isPresent()) {
            Event eventEntity = event.get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedDate = eventEntity.getEventDate().format(formatter);
            model.addAttribute("event", eventEntity);
            model.addAttribute("formattedDate", formattedDate);
            return "edit_event_page";
        } else {
            return "redirect:/mod_panel?error=EventNotFound";
        }
    }

    @PostMapping("/admin/edit-event")
    public String editEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("eventDate") String eventDate,
            @RequestParam("price") double price) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime eventDateTime = LocalDateTime.parse(eventDate, formatter);

            adminService.editEvent(eventId, title, description, category, image, eventDateTime, price);

            return "redirect:/mod_panel";
        } catch (IOException e) {
            e.printStackTrace();
            return "error_page";
        }
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

        DecimalFormat df = new DecimalFormat("0.00");
        String formattedTotalAmount = df.format(totalAmount);

        model.addAttribute("totalAmount", formattedTotalAmount);
    }

    @GetMapping("/my_orders")
    public String myOrdersPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
            addCartTotalToModel(session, model);
        } else {
            return "redirect:/login";
        }

        List<Order> userOrders = orderService.getOrdersByUserId(loggedInUser.getId());

        DecimalFormat df = new DecimalFormat("0.00");
        for (Order order : userOrders) {
            double totalPrice = order.getTotalPrice();
            String formattedTotalPrice = df.format(totalPrice);
            order.setFormattedTotalPrice(formattedTotalPrice);
        }

        model.addAttribute("userOrders", userOrders);

        return "my_orders_page";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam("eventId") List<Long> eventIds,
                             @RequestParam("quantity") List<Integer> quantities,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            for (int i = 0; i < eventIds.size(); i++) {
                orderService.createOrder(loggedInUser.getId(), eventIds.get(i), quantities.get(i));
            }
            session.setAttribute("cart", null);
            redirectAttributes.addFlashAttribute("successMessage", "Zamówienie zostało złożone. Dziękujemy!");
            return "redirect:/placeOrder/success";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił błąd podczas składania zamówienia. Proszę spróbować ponownie.");
            return "redirect:/placeOrder/error";
        }
    }

    @GetMapping("/placeOrder/success")
    public String placeOrderSuccessPage() {
        return "place_order_success_page";
    }

}
