package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.User;

public interface UserManagerInterface
{
    User findUserByUsername(String username);
}
