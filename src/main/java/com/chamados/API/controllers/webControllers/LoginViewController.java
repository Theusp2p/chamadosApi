package com.chamados.API.controllers.webControllers;

import com.chamados.API.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String homePage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication instanceof CustomAuthentication customAuth) {
                // Verifica se é ADMIN
                boolean isAdmin = authentication.getAuthorities()
                        .stream()
                        .anyMatch(grantedAuthority ->
                                grantedAuthority.getAuthority().equals("ADMIN"));

                return isAdmin ? "redirect:/admin/dashboard" : "redirect:/user/dashboard";
            }
        }
        // Se não autenticado, redireciona para login
        return "redirect:/login";
    }
}