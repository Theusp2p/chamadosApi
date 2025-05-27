package com.chamados.API.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.chamados.API.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.department.name = 'Tecnologia da Informação'")
    List<User> findByTechnicals();


    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))")

    List<User> searchUsers(@Param("search") String search);
}
