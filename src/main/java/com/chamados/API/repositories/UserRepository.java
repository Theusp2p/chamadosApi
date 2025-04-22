package com.chamados.API.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chamados.API.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);
}
