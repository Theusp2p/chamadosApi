package com.chamados.API.controllers;

import com.chamados.API.entities.Client;
import com.chamados.API.repositories.ClientRepository;
import com.chamados.API.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void insertClient(@RequestBody Client client) {
        clientService.insert(client);
    }

}
