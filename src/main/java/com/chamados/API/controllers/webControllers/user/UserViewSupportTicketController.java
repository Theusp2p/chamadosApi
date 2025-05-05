package com.chamados.API.controllers.webControllers.user;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.services.DepartmentService;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/chamados")
public class UserViewSupportTicketController {

    private final DepartmentService departmentService;
    private final SupportTicketService supportTicketService;


    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", supportTicketService.create());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/users/new";
    }

}
