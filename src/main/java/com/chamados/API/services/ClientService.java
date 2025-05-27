package com.chamados.API.services;

import com.chamados.API.entities.Client;
import com.chamados.API.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public void insert(Client client) {
        var encryptedPassword = passwordEncoder.encode(client.getClientSecret());
        client.setClientSecret(encryptedPassword);
        clientRepository.save(client);
    }

    public Client findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
