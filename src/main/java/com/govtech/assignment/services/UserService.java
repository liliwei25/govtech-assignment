package com.govtech.assignment.services;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.entities.User;
import com.govtech.assignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findUsers(UserGetRequest request) {
        return this.userRepository.findUsersFromRequest(request);
    }
}
