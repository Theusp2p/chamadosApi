package com.chamados.API.controllers;

import com.chamados.API.controllers.dtos.UserDTO;
import com.chamados.API.controllers.mappers.UserMapper;
import com.chamados.API.entities.User;
import com.chamados.API.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements GenericController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> insertUser(@RequestBody @Valid UserDTO userDTO){
        User user = userService.insertUser(userMapper.toEntity(userDTO));
        return ResponseEntity.created(generateHeaderLocation(user.getId())).build();
    }

    @GetMapping
    public ResponseEntity<UserDTO> getByUsername(@RequestParam String username){
        var user = userService.getByUsername(username);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
}
