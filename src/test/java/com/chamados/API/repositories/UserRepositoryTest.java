package com.chamados.API.repositories;

import com.chamados.API.controllers.DepartmentRepository;
import com.chamados.API.entities.Department;
import com.chamados.API.entities.User;
import com.chamados.API.entities.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void isertUser(){
        User user = new User();
        user.setName("admin");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setIsActive(true);
        user.setCreatedDate(LocalDateTime.now());
        user.setRole(UserRole.ADMIN);

        Department department = departmentRepository.findById(2).get();

        user.setDepartment(department);

        userRepository.save(user);
    }

    @Test
    void updateUser(){
        User user = userRepository.findById(3).get();
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setLastModifiedDate(LocalDateTime.now());
        userRepository.save(user);


    }


}