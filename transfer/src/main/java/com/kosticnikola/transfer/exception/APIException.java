package com.kosticnikola.transfer.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class APIException {
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;

    public APIException(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public APIException(Map<String, String> validationErrors) {
        this.timestamp = LocalDateTime.now();
        this.validationErrors = validationErrors;
    }
}
