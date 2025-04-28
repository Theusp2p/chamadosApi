package com.chamados.API.repositories;

import com.chamados.API.entities.Client;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void insertClient() {
        Client client = new Client();
        client.setClientId("meu-client");
        client.setClientSecret(passwordEncoder.encode("client-secret"));
        client.setRedirectURI("http://localhost:8080/authorized");
        client.setScope("ADMIN");

        clientRepository.save(client);
    }

}