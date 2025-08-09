package com.chamados.API.controllers.webControllers;

import com.chamados.API.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String homePage(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();

            // Verifica se precisa trocar a senha
            if (user.getIsChangePasswordNextLogin()) {
                return "redirect:/resetar-senha";
            }

            // Verifica se é ADMIN
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ADMIN"));

            return isAdmin ? "redirect:/admin/dashboard" : "redirect:/user/dashboard";
        }
        return "redirect:/login";
    }
}