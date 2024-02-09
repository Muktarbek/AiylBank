package com.muktars.aiylbank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Setter
@Getter
@AllArgsConstructor
public class CustomErrorResponse {
    private HttpStatus status;
    private String message;
}
