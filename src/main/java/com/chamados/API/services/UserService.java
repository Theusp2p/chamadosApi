package com.chamados.API.services;


import com.chamados.API.entities.User;
import com.chamados.API.exceptions.UsernameAlreadyExistsException;
import com.chamados.API.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User insertUser(User user){

        if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyExistsException("O usuário " + user.getUsername() + "já está em uso");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User GetByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
