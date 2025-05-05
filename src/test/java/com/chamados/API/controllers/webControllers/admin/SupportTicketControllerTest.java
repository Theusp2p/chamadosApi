package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.Department;
import com.chamados.API.entities.SupportTicket;
import com.chamados.API.entities.User;
import com.chamados.API.services.DepartmentService;
import com.chamados.API.services.SupportTicketService;
import com.chamados.API.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SupportTicketControllerTest {

    @Autowired
    private SupportTicketService supportTicketService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;


    @Test
    void registerSupportTicket() {
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setObject("Xcelera não transfere para o outro computador");
        supportTicket.setDescription("Já enviei mais de três vezes e nada");

        Department department = departmentService.findById(2);
        supportTicket.setDepartment(department);

        User createdBy = userService.getByUsername("admin");

        supportTicketService.create(supportTicket, createdBy);
    }

}