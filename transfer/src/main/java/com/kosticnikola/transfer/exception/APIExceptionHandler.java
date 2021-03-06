package com.kosticnikola.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), StringUtils.capitalize(error.getDefaultMessage()))
        );
        return new ResponseEntity<>(new APIException(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpClientErrorException.class, InvalidIDException.class})
    public ResponseEntity<?> handleInvalidIDException() {
        return new ResponseEntity<>(new APIException("Invalid team/player id(s)."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerErrorExceptions() {
        return new ResponseEntity<>(new APIException("Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
