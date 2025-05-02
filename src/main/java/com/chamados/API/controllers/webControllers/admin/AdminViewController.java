package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import com.chamados.API.security.CustomAuthentication;
import com.chamados.API.services.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final SupportTicketService ticketService;

    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        // Adiciona os dados do usuário autenticado
        if (authentication instanceof CustomAuthentication customAuth) {
            model.addAttribute("user", customAuth.getUser());
        }

        // Adiciona os dados para o dashboard
        model.addAttribute("openTicketsCount", ticketService.countOpenTickets());
        model.addAttribute("resolvedTicketsCount", ticketService.countResolvedTickets());
        model.addAttribute("inProgressTicketsCount", ticketService.countInProgressTickets());
        model.addAttribute("urgentTicketsCount", ticketService.countUrgentTickets());
        model.addAttribute("urgentTickets", ticketService.findUrgentTickets());

        return "admin/dashboard"; // Agora aponta para o template único
    }
}
