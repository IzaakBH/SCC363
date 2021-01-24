package com.scc363.hospitalproject.utils;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.datamodels.dtos.UserDTO;

public class DTOMapper {

    public static User userDtoToEntity(@javax.validation.Valid User u) {
        return new User(u.getUsername(), u.getPassword(), u.getEmail(), u.getUserType());
    }

    public static UserDTO userEntityToDto(User u) {
        return new UserDTO(u.getUsername(), u.getPassword(), u.getEmail(), u.getUserType());
    }
}
