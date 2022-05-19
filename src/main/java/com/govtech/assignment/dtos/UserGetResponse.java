package com.govtech.assignment.dtos;

import com.govtech.assignment.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetResponse {
    private Iterable<User> results;

    public UserGetResponse(Iterable<User> results) {
        this.results = results;
    }
}
