package com.kosticnikola.player.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), StringUtils.capitalize(error.getDefaultMessage()))
        );
        return new ResponseEntity<>(new APIException(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIDException.class)
    public ResponseEntity<?> handleInvalidIDException() {
        return new ResponseEntity<>(new APIException("Player with provided id doesn't exist."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException() {
        return new ResponseEntity<>(new APIException("Player with provided UPIN already exists."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerErrorExceptions() {
        return new ResponseEntity<>(new APIException("Internal server error."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
