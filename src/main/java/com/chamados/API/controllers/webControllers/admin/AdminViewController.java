package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.security.CustomAuthentication;
import com.chamados.API.services.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final SupportTicketService ticketService;

    @GetMapping("/dashboard")
    public String adminDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            Model model) {

        // Adiciona os dados do usuário autenticado ao modelo
        if (authentication instanceof CustomAuthentication(com.chamados.API.entities.User user)) {
            model.addAttribute("user", user);
        }

        // Chama o serviço com paginação
        Page<SupportTicket> paginatedTickets = ticketService.findOpenTicketsPaginated(page, size);

        model.addAttribute("activePage", "dashboard");
        model.addAttribute("openTicketsCount", ticketService.countOpenTickets());
        model.addAttribute("opensTickets", paginatedTickets.getContent()); // lista da página atual
        model.addAttribute("currentPage", page); // página atual
        model.addAttribute("totalPages", paginatedTickets.getTotalPages()); // total de páginas

        return "admin/dashboard";
    }

}
