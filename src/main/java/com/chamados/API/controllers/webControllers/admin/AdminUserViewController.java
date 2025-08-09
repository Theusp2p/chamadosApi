package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.controllers.dtos.UserDTO;
import com.chamados.API.controllers.mappers.UserMapper;
import com.chamados.API.entities.User;
import com.chamados.API.services.DepartmentService;
import com.chamados.API.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class AdminUserViewController {

    private final UserService userService;
    private final DepartmentService departmentService;
    private final UserMapper userMapper;

    @GetMapping
    public String listUsers(
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        List<User> users = userService.searchUsers(search);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        model.addAttribute("activePage", "usuarios");
        return "admin/users/list";
    }

    @GetMapping("/novo")
    public String showCreateForm(Model model) {
        User user = new User();
        user.setIsChangePasswordNextLogin(true); // Novo usuário deve trocar senha
        model.addAttribute("user", user);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("activePage", "usuarios");
        return "admin/users/new";
    }

    @GetMapping("/{id}/editar")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("activePage", "usuarios");
        model.addAttribute("departments", departmentService.findAll());
        return "admin/users/edit";
    }

    @PostMapping("/{id}/atualizar")
    @Transactional
    public String saveEdit(@PathVariable Long id,
                           @ModelAttribute("user") User updatedUser,
                           @RequestParam(required = false) String action,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {


        // Ativação ou desativação explícita via botão ou formulário
        if ("deactivate".equals(action)) {
            updatedUser.setIsActive(false);
        } else if ("activate".equals(action)) {
            updatedUser.setIsActive(true);
        }

        updatedUser.setLastModifiedBy(authentication.getName());

        userService.updateUser(id, updatedUser);
        redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/salvar")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(required = false) String action,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                User currentUser = (User) authentication.getPrincipal();

                if (user.getId() == null) {
                    user.setCreatedBy(currentUser.getName());
                }
            }

            // Validações
            if (user.getUsername().length() > 15) {
                redirectAttributes.addFlashAttribute("error", "O username deve ter no máximo 15 caracteres");
                return "redirect:/admin/usuarios/novo";
            }

            user.setUsername(user.getUsername().toLowerCase());

            if (user.getId() == null && userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "Já existe um usuário com este username");
                model.addAttribute("departments", departmentService.findAll());
                return "admin/users/new";
            }

            userService.insertUser(user);
            redirectAttributes.addFlashAttribute("success", "Usuário salvo com sucesso!");
            return "redirect:/admin/usuarios";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar usuário: " + e.getMessage());
            return "redirect:/admin/usuarios/novo";
        }
    }

}