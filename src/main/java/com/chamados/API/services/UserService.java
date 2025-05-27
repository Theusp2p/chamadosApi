package com.chamados.API.services;


import com.chamados.API.entities.User;
import com.chamados.API.exceptions.UsernameAlreadyExistsException;
import com.chamados.API.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
            updatedUser.setName(updatedUser.getName());
            updatedUser.setUsername(updatedUser.getUsername());
            updatedUser.setPassword(updatedUser.getPassword());
            updatedUser.setDepartment(updatedUser.getDepartment());
            updatedUser.setIsActive(updatedUser.getIsActive());
            updatedUser.setRole(updatedUser.getRole());
            updatedUser.setIsChangePasswordNextLogin(updatedUser.getIsChangePasswordNextLogin());
            updatedUser.setCreatedBy(userFound.get().getCreatedBy());
            updatedUser.setCreatedAt(userFound.get().getCreatedAt());
            userRepository.save(updatedUser);
        }
    }
}
