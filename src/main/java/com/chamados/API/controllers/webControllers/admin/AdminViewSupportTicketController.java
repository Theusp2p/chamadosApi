package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/chamados")
@RequiredArgsConstructor
public class AdminViewSupportTicketController {

    private final SupportTicketService ticketService;
    private final UserService userService;
    private final SupportTicketService supportTicketService;

        @GetMapping
        public String listarChamados(
                @RequestParam(required = false) String status,
                Model model
        ) {
            model.addAttribute("activePage", "chamados");
            model.addAttribute("openTicketsCount", ticketService.countOpenTickets());
            model.addAttribute("resolvedTicketsCount", ticketService.countResolvedTickets());
            model.addAttribute("inProgressTicketsCount", ticketService.countInProgressTickets());
            model.addAttribute("urgentTicketsCount", ticketService.countUrgentTickets());
            model.addAttribute("urgentTickets", ticketService.findUrgentTickets());
            model.addAttribute("inProgressTickets", ticketService.findInProgressTickets());
            return "admin/chamados/list";
        }

        @GetMapping("/{id}/editar")
        public String editTicket(Model model, @PathVariable long id) {
            SupportTicket supportTicket = ticketService.findById(id);
            List<User> technical = userService.findByTechnicals();

            model.addAttribute("ticket", supportTicket);
            model.addAttribute("technical", technical);
            model.addAttribute("activePage", "chamados");
            return "admin/chamados/edit";
        }

        @PostMapping("/{id}/editar")
        @Transactional
        public String saveEdit(@PathVariable long id, @ModelAttribute("ticket") SupportTicket updatedTicket, RedirectAttributes redirectAttributes) {
            supportTicketService.updateTicket(id, updatedTicket);
            redirectAttributes.addFlashAttribute("sucess", "Chamado atualizado com sucesso!");
            return "redirect:/admin/dashboard";
        }

    }