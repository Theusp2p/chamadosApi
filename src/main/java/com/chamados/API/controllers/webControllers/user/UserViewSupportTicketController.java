package com.chamados.API.controllers.webControllers.user;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.services.DepartmentService;
import com.chamados.API.services.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user/chamados")
public class UserViewSupportTicketController {

    private final DepartmentService departmentService;
    private final SupportTicketService supportTicketService;


    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new SupportTicket());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("activePage", "usuarios");
        return "user/chamados/new";
    }

    @PostMapping("/salvar")
    public String CreateSupportTicket(@ModelAttribute SupportTicket ticket,
                           @RequestParam(required = false) String action,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        if (authentication != null && authentication.isAuthenticated()) {
            var currentUser = authentication.getName();

                ticket.setCreatedBy(currentUser);

        }

        supportTicketService.create(ticket);
        redirectAttributes.addFlashAttribute("success", "Seu chamado foi registrado com sucesso!");
        return "redirect:/user/dashboard";
    }

}
