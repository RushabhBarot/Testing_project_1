package com.SpringBoot.TestingHW.advises;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiError {
    private String message;
    private HttpStatus httpStatus;
    private List<String> subErrors;
}
