package com.eventmanager.event_management.Controller;

import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login_page";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        RedirectAttributes redirectAttributes, HttpSession session) {
        if (userService.authenticate(username, password)) {
            User loggedInUser = userService.getUserByUsername(username);
            System.out.println("Zalogowano: " + loggedInUser);
            Long userId = loggedInUser.getId();
            String loggedRole = userService.getRole(userId);
            System.out.println("Rola: " + loggedRole);
            session.setAttribute("loggedInUser", loggedInUser);
            session.setAttribute("role", loggedRole);
            redirectAttributes.addFlashAttribute("successMessage", "Zalogowano pomyślnie");
            return "redirect:/login/success";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd logowania");
            return "redirect:/login";
        }
    }

    @GetMapping("/login/success")
    public String success() {
        return "login_success_page";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes redirectAttributes) {
        Object loggedInUser = session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            session.removeAttribute("loggedInUser");
        }
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Wylogowano pomyślnie");
        return "redirect:/logout/success";
    }

    @GetMapping("/logout/success")
    public String logoutSuccess() {
        return "logout_success_page";
    }
}
