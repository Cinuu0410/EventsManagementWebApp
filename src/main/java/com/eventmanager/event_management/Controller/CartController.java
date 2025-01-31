package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.CartItem;
import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.EventService;
import com.eventmanager.event_management.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long eventId, @RequestParam int quantity, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getEventId().equals(eventId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            Event event = eventService.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wydarzenia o ID: " + eventId));
            CartItem cartItem = new CartItem();
            cartItem.setEventId(event.getId());
            cartItem.setEventName(event.getTitle());
            cartItem.setPrice(event.getPrice());
            cartItem.setQuantity(quantity);
            cart.add(cartItem);
        }

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
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

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        DecimalFormat df = new DecimalFormat("0.00");

        for (CartItem item : cart) {
            String formattedPrice = df.format(item.getPrice());
            item.setFormattedPrice(formattedPrice);
            String formattedTotalPrice = df.format(item.getTotalPrice());
            item.setFormattedTotalPrice(formattedTotalPrice);
        }

        addCartTotalToModel(session, model);
        model.addAttribute("cart", cart);

        return "cart";
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
}
