package com.chamados.API.controllers.webControllers.user;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.services.DepartmentService;
import com.chamados.API.services.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/chamados")
public class UserViewSupportTicketController {

    private final DepartmentService departmentService;
    private final SupportTicketService supportTicketService;

    @GetMapping("/meus-chamados")
    public String myTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            Model model) {

        Page<SupportTicket> paginatedTickets = supportTicketService.getMyTickets(page, size, authentication);

        model.addAttribute("activePage", "meus-chamados");
        model.addAttribute("tickets", paginatedTickets);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "user/chamados/meus-chamados"; // Novo template
    }

    @GetMapping
    public String getMyTicketsTableFragment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            Model model) {

        Page<SupportTicket> paginatedTickets = supportTicketService.getMyTickets(page, size, authentication);

        model.addAttribute("tickets", paginatedTickets.getContent());

        return "user/dashboard :: #tickets-table";
    }

    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new SupportTicket());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("activePage", "usuarios");
        return "user/chamados/new";
    }

    @PostMapping("/salvar")
    public String createSupportTicket(
            @ModelAttribute SupportTicket ticket,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (authentication != null && authentication.isAuthenticated()) {
            ticket.setCreatedBy(authentication.getName());
        }

        supportTicketService.create(ticket);
        redirectAttributes.addFlashAttribute("success", "Seu chamado foi registrado com sucesso!");
        return "redirect:/user/dashboard";
    }

    @GetMapping("/dashboard/counters")
    @ResponseBody
    public Map<String, Object> getDashboardCounters() {
        return Map.of(
                "open", supportTicketService.countOpenTickets(),
                "inProgress", supportTicketService.countInProgressTickets(),
                "urgent", supportTicketService.countUrgentTickets()
        );
    }
    @GetMapping("/latest")
    @ResponseBody
    public Map<String, Long> getLatestTicketId() {
        SupportTicket latest = supportTicketService.findLatestTicket();
        return Collections.singletonMap("id", latest != null ? latest.getId() : 0L);
    }
}