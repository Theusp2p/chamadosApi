package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.security.CustomAuthentication;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final SupportTicketService ticketService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        // Adiciona os dados do usuário autenticado
        if (authentication instanceof CustomAuthentication customAuth) {
            model.addAttribute("user", customAuth.user());
        }

        model.addAttribute("activePage", "dashboard");
        model.addAttribute("openTicketsCount", ticketService.countOpenTickets());
        model.addAttribute("opensTickets", ticketService.filterByStatusOpen());

        return "admin/dashboard"; // Agora aponta para o template único
    }
}
