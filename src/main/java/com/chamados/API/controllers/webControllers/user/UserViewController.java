package com.chamados.API.controllers.webControllers.user;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.security.CustomAuthentication;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

    private final SupportTicketService ticketService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String userDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            Model model) {

        // Adiciona os dados do usuário autenticado ao modelo
        if (authentication instanceof CustomAuthentication customAuth) {
            model.addAttribute("user", customAuth.user());
        }

        // Chama o serviço com paginação
        Page<SupportTicket> paginatedTickets = ticketService.findOpenTicketsPaginated(page, size);

        model.addAttribute("activePage", "dashboard");
        model.addAttribute("openTicketsCount", ticketService.countOpenTickets());
        model.addAttribute("tickets", paginatedTickets.getContent()); // Padronizado para "tickets"
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedTickets.getTotalPages());
        model.addAttribute("size", size); // Adicionado para manter consistência na paginação

        return "user/dashboard";
    }
}
