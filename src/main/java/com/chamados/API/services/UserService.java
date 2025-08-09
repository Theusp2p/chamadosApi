package com.chamados.API.services;


import com.chamados.API.entities.User;
import com.chamados.API.exceptions.UsernameAlreadyExistsException;
import com.chamados.API.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void insertUser(User user){

        if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyExistsException(STR."O usuário \{user.getUsername()}já está em uso");
        }

        user.setCreatedAt(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByTechnicals() {
        return userRepository.findByTechnicals();
    }

    public List<User> searchUsers(String search) {
       return userRepository.searchUsers(search);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username.toLowerCase());
    }

    public void updateUser(Long id, User updatedUser) {
        Optional<User> userFound = userRepository.findById(id);
        if(userFound.isPresent()){
            User existingUser = userFound.get();

            // Atualiza apenas os campos permitidos
            existingUser.setName(updatedUser.getName());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setDepartment(updatedUser.getDepartment());
            existingUser.setIsActive(updatedUser.getIsActive());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setIsChangePasswordNextLogin(updatedUser.getIsChangePasswordNextLogin());

            // Mantém a senha existente se a nova for null
            if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // Mantém campos de auditoria originais
            existingUser.setLastModifiedBy(updatedUser.getLastModifiedBy());
            existingUser.setLastModifiedAt(LocalDateTime.now());

            userRepository.save(existingUser);
        }
    }

    @Transactional
    public void resetPassword(Long userId, String currentPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsChangePasswordNextLogin(false);
        userRepository.save(user);
    }

}
