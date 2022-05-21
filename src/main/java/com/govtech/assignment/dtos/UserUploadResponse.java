package com.govtech.assignment.dtos;

import com.govtech.assignment.enums.RequestSuccess;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUploadResponse {
    private RequestSuccess success = RequestSuccess.FAILED;
    private String error;
}
