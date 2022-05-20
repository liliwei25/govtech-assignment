package com.govtech.assignment.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    private String error;

    public ApiError(String error) {
        super();
        this.error = error;
    }
}
