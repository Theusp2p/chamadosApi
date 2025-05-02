package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import com.chamados.API.security.CustomAuthentication;
import com.chamados.API.services.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/chamados")

@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService ticketService;

}