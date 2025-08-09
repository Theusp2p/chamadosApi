package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/chamados")
@RequiredArgsConstructor
public class AdminViewSupportTicketController {

    private final SupportTicketService ticketService;
    private final UserService userService;

        @GetMapping
        public String listarChamados(
                @RequestParam(required = false) String status,
                Model model
        ) {
            model.addAttribute("activePage", "chamados");
            model.addAttribute("openTicketsCount", ticketService.countOpenTickets());
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
        public String saveEdit(@PathVariable long id, @ModelAttribute("ticket") SupportTicket updatedTicket, RedirectAttributes redirectAttributes, Authentication authentication) {
            updatedTicket.setLastModifiedBy(authentication.getName());
            ticketService.updateTicket(id, updatedTicket);
            redirectAttributes.addFlashAttribute("success", "Chamado atualizado com sucesso!");
            return "redirect:/admin/chamados";
        }

        @GetMapping("/latest")
        public ResponseEntity<Map<String, Long>> getLatestTicket() {
            SupportTicket latestTicket = ticketService.findLatestTicket();
            return ResponseEntity.ok(
                    Collections.singletonMap("id", latestTicket != null ? latestTicket.getId() : 0)
            );
        }

        @GetMapping("/dashboard/counts")
        @ResponseBody
        public Map<String, Object> getTicketCounts() {
            return Map.of(
                    "open", ticketService.countOpenTickets(),
                    "inProgress", ticketService.countInProgressTickets(),
                    "urgent", ticketService.countUrgentTickets()
            );
        }

        @GetMapping("/recent")
        public String getRecentTickets(Model model) {
            model.addAttribute("opensTickets", ticketService.findOpenTicketsPaginated(0, 10).getContent());
            return "admin/dashboard :: #tickets-table"; // Retorna apenas o fragmento da tabela
        }

    }