package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.entities.enums.PriorityRole;
import com.chamados.API.entities.enums.SupportTicketStatusRole;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.AbstractAuditable_.createdBy;

@SpringBootTest
class SupportTicketControllerTest {

    @Autowired
    private SupportTicketService supportTicketService;
    @Autowired
    private UserService userService;


    @Test
    void registerSupportTicket() {
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setObject("Teste");
        supportTicket.setDescription("testando");
        supportTicket.setPriority(PriorityRole.BAIXA);

        User createdBy = userService.getByUsername("admin");

        supportTicketService.create(supportTicket, (User) createdBy);
    }

}