package com.chamados.API.controllers.dtos;

import com.chamados.API.entities.Department;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(

        @NotBlank(message = "campo obrigatório")
        String name,

        @NotBlank(message = "campo obrigatório")
        @Size(max = 15)
        String username,

        @NotBlank(message = "campo obrigatório")
        @Size(min = 4, max = 10)
        String password,

        @NotNull @Valid
        Department department,

        @NotBlank(message = "campo obrigatório")
        Boolean isActive,

        @NotNull(message = "campo obrigatório")
        String role

) {
}
