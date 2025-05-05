package com.chamados.API.controllers.webControllers.user;

import com.chamados.API.security.CustomAuthentication;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

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

        return "user/dashboard"; // Agora aponta para o template único
    }

}
