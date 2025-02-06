package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Enum.OrderStatus;
import com.eventmanager.event_management.Model.Breadcrumb;
import com.eventmanager.event_management.Model.Order;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.OrderService;
import com.eventmanager.event_management.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/manage-orders")
    public String manageOrders(Model model, HttpSession session) {
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
                new Breadcrumb("Panel moderatora", "/mod_panel"),
                new Breadcrumb("Zarządzanie zamówieniami", "/manage-orders")
        ));

        String assignedCategory = loggedInUser.getAssignedCategory();
        System.out.println("Kategoria: " + assignedCategory);

        List<Order> orders = orderService.getOrdersByCategory(assignedCategory);

        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("orders", orders);
        model.addAttribute("assignedCategory", assignedCategory);

        return "manage_orders_page";
    }

    @PostMapping("/manage-orders/{orderId}/change-status")
    public String changeOrderStatus(@PathVariable Long orderId,
                                    @RequestParam("status") String status,
                                    HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            orderService.changeOrderStatus(orderId, orderStatus);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/manage-orders?error=true";
        }

        return "redirect:/manage-orders";
    }
}
