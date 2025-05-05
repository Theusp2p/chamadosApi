package com.chamados.API.repositories;

import com.chamados.API.entities.Department;
import com.chamados.API.entities.User;
import com.chamados.API.entities.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

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
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(UserRole.ADMIN);
        user.setCreatedBy("admin");

        Department department = departmentRepository.findById(1).get();

        user.setDepartment(department);

        userRepository.save(user);
    }



    @Test
    void updateUser(){
        User user = userRepository.findById(3L).get();
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setLastModifiedAt(LocalDateTime.now());
        userRepository.save(user);


    }


}