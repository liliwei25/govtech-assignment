package com.govtech.assignment.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserUploadRequest {
    private MultipartFile file;
}
