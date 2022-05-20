package com.govtech.assignment.controllers;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.dtos.UserGetResponse;
import com.govtech.assignment.entities.User;
import com.govtech.assignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<UserGetResponse> getAllUsers(@Valid UserGetRequest request) {
        List<User> users = userService.findUsers(request);
        return ResponseEntity.status(HttpStatus.OK).body(new UserGetResponse(users));
    }
}
