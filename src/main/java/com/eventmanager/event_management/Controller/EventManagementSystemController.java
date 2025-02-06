package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.*;
import com.eventmanager.event_management.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private ColorThemeService colorThemeService;
    @Autowired
    private EmailService emailService;

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
        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Admin panel", "/admin_panel")
        ));
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
        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Panel administratora", "/admin_panel"),
                new Breadcrumb("Dodaj nowe wydarzenie", "/admin/create-event-page")
        ));
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
        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Panel administratora", "/admin_panel"),
                new Breadcrumb("Dodaj zdjęcia do bazy", "/admin/upload-images-to-database")
        ));
        return "upload_images_to_database_page";
    }

    @GetMapping("/admin/set-image-description")
    public String setImageDescriptionPage(Model model, HttpSession session) {
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
        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Panel administratora", "/admin_panel"),
                new Breadcrumb("Ustaw opisy do zdjęć", "/admin/set-image-description")
        ));
        List<SliderImage> allImages = sliderImageService.getAllSliderImages();
        model.addAttribute("allImages", allImages);

        return "set_image_description_page";
    }

    @PostMapping("/admin/update-image-description")
    public String updateImageDescription(@RequestParam("imageId") Long imageId,
                                         @RequestParam("description") String description) {
        SliderImage image = sliderImageService.getImageById(imageId);
        if (image != null) {
            image.setDescription(description);
            sliderImageService.saveImageSecond(image);
        }
        return "redirect:/admin/set-image-description";
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
        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Panel administratora", "/admin_panel"),
                new Breadcrumb("Edycja slidera", "/admin/edit-slider")
        ));
        List<SliderImage> allImages = sliderImageService.getAllSliderImages();
        model.addAttribute("allImages", allImages);
        return "edit_slider_page";
    }

    @PostMapping("/slider/update")
    public String updateSliderImages(
            @RequestParam(value = "selectedImages", required = false) List<Long> selectedImages,
            @RequestParam("imageOrder") String imageOrder) {

        List<Long> imageOrderList = Arrays.stream(imageOrder.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        sliderImageService.updateSliderImages(selectedImages, imageOrderList);

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

        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Panel administratora", "/admin_panel"),
                new Breadcrumb("Przydzielanie kategorii moderatorowi", "/admin/set-category-to-mod")
        ));

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
            StringBuilder orderDetails = new StringBuilder();
            for (int i = 0; i < eventIds.size(); i++) {
                Event event = eventService.getEventById(eventIds.get(i));

                if (event != null) {
                    orderDetails.append("Wydarzenie: ").append(event.getTitle())
                            .append(", Ilość biletów: ").append(quantities.get(i))
                            .append("\n");

                    orderService.createOrder(loggedInUser.getId(), eventIds.get(i), quantities.get(i));
                } else {
                    orderDetails.append("Wydarzenie o ID ").append(eventIds.get(i))
                            .append(" nie zostało znalezione.\n");
                }
            }
            emailService.sendOrderConfirmationEmail(loggedInUser.getEmail(), orderDetails.toString());

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

    @GetMapping("/admin/change-colors")
    public String changeColorsPage(Model model, HttpSession session) {
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
        model.addAttribute("breadcrumb", Arrays.asList(
                new Breadcrumb("Strona główna", "/main_page"),
                new Breadcrumb("Panel administratora", "/admin_panel"),
                new Breadcrumb("Zarządzanie kolorami", "/admin/change-colors")
        ));
        return "change_colors_page";
    }

    @PostMapping("/admin/update-colors")
    public String updateColors(@RequestParam String primaryColor,
                               @RequestParam String secondaryColor,
                               @RequestParam String buttonColor) throws IOException {

        Path path = Paths.get("src/main/resources/static/css/main_page_style.css");

        String cssContent = Files.readString(path);

        cssContent = cssContent.replaceAll("(?<=--primary-color: )#[0-9a-fA-F]{6}", primaryColor)
                .replaceAll("(?<=--accent-color: )#[0-9a-fA-F]{6}", secondaryColor)
                .replaceAll("(?<=--neutral-color: )#[0-9a-fA-F]{6}", buttonColor);

        Files.writeString(path, cssContent);

        return "redirect:/main_page";
    }

    @PostMapping("/admin/reset-colors")
    public String resetColors() throws IOException {
        Path cssFilePath = Paths.get("src/main/resources/static/css/main_page_style.css");

        String cssContent = Files.readString(cssFilePath, StandardCharsets.UTF_8);

        cssContent = cssContent.replaceAll("(?<=--primary-color: )#[0-9a-fA-F]{6}", "#041d75")
                .replaceAll("(?<=--accent-color: )#[0-9a-fA-F]{6}", "#6588c8")
                .replaceAll("(?<=--neutral-color: )#[0-9a-fA-F]{6}", "#2c3e50");

        Files.writeString(cssFilePath, cssContent, StandardCharsets.UTF_8);

        return "redirect:/main_page";
    }

    @PostMapping("/admin/add-theme")
    public String addTheme(@RequestParam String name,
                           @RequestParam String primaryColor,
                           @RequestParam String secondaryColor,
                           @RequestParam String buttonColor) {

        colorThemeService.save(name, primaryColor, secondaryColor, buttonColor);

        return "redirect:/admin_panel";
    }

    @PostMapping("/update-theme")
    public String updateTheme(@RequestParam Long themeId, HttpSession session) throws IOException {
        ColorTheme theme = colorThemeService.getThemeById(themeId);

        if (theme != null) {
            session.setAttribute("selectedTheme", theme);
        } else {
            session.setAttribute("themeError", "Motyw o podanym ID nie istnieje.");
        }

        Path path = Paths.get("src/main/resources/static/css/my_profile_page_style.css");

        String cssContent = Files.readString(path);

        assert theme != null;
        cssContent = cssContent.replaceAll("(?<=--primary-color: )#[0-9a-fA-F]{6}", theme.getPrimaryColor())
                .replaceAll("(?<=--accent-color: )#[0-9a-fA-F]{6}", theme.getSecondaryColor())
                .replaceAll("(?<=--neutral-color: )#[0-9a-fA-F]{6}", theme.getButtonColor());

        Files.writeString(path, cssContent);


        return "redirect:/my_profile";
    }

    @GetMapping("/my_profile")
    public String myProfilePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("role", loggedRole);
            addCartTotalToModel(session, model);
            List<ColorTheme> colorThemes = colorThemeService.getAllThemes();
            model.addAttribute("colorThemes", colorThemes);

        } else {
            return "redirect:/login";
        }
        return "my_profile_page";
    }

    @PostMapping("/reset-user-colors")
    public String resetUserColors() throws IOException {
        Path cssFilePath = Paths.get("src/main/resources/static/css/my_profile_page_style.css");

        String cssContent = Files.readString(cssFilePath, StandardCharsets.UTF_8);

        cssContent = cssContent.replaceAll("(?<=--primary-color: )#[0-9a-fA-F]{6}", "#041d75")
                .replaceAll("(?<=--accent-color: )#[0-9a-fA-F]{6}", "#6588c8")
                .replaceAll("(?<=--neutral-color: )#[0-9a-fA-F]{6}", "#2c3e50");

        Files.writeString(cssFilePath, cssContent, StandardCharsets.UTF_8);

        return "redirect:/my_profile";
    }
}
