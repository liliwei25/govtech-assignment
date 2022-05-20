package com.govtech.assignment.repositories;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.entities.User;

import java.util.List;

public interface CustomUserRepository {
    List<User> findUsersFromRequest(UserGetRequest request);
}
