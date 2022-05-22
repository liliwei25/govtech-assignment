package com.govtech.assignment.controllers;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.dtos.UserGetResponse;
import com.govtech.assignment.dtos.UserUploadRequest;
import com.govtech.assignment.dtos.UserUploadResponse;
import com.govtech.assignment.entities.User;
import com.govtech.assignment.enums.RequestSuccess;
import com.govtech.assignment.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Controller
@Validated
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(path="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<UserGetResponse> getAllUsers(@Valid UserGetRequest request) {
        List<User> users = userService.findUsers(request);
        return ResponseEntity.status(HttpStatus.OK).body(new UserGetResponse(users));
    }

    @PostMapping(
            path="/upload",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody
    DeferredResult<ResponseEntity<UserUploadResponse>> uploadUsers(UserUploadRequest request) {
        DeferredResult<ResponseEntity<UserUploadResponse>> output = new DeferredResult<>();
        logger.info("Processing upload request...");
        ForkJoinPool.commonPool().submit(() -> {
            UserUploadResponse response = new UserUploadResponse();
            try {
                this.userService.saveAllUsersFromRequest(request);
                response.setSuccess(RequestSuccess.SUCCESS);
                output.setResult(ResponseEntity.status(HttpStatus.OK).body(response));
                logger.info("Upload request processed successfully");
            } catch (IOException e) {
                response.setError(e.getMessage());
                output.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
                logger.error("Failed to process upload request: " + e.getMessage());
            }
        });
        return output;
    }
}
