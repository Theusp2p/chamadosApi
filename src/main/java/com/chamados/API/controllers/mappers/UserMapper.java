package com.chamados.API.controllers.mappers;

import com.chamados.API.controllers.dtos.UserDTO;
import com.chamados.API.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);
}