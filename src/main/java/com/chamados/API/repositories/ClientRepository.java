package com.chamados.API.repositories;

import com.chamados.API.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client findByClientId(String clientId);
}
