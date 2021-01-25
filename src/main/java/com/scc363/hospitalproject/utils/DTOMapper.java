package com.scc363.hospitalproject.utils;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.datamodels.dtos.UserDTO;

import javax.validation.Valid;

public class DTOMapper {

    public static User userDtoToEntity(@Valid UserDTO u) {
        return new User(u.getUsername(), u.getPassword(), u.getEmail(), u.getUserType(), u.getFirst(), u.getLast());
    }

    public static UserDTO userEntityToDto(User u) {
        return new UserDTO(u.getUsername(), u.getPassword(), u.getEmail(), u.getUserType(), u.getFirst(), u.getLast());
    }
}
