package com.example.springapptest.controller;

import com.example.springapptest.dto.ExceptionResponse;
import io.jsonwebtoken.MalformedJwtException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.NotContextException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleAllException(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request){
        ExceptionResponse response= new ExceptionResponse(ex.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(MalformedJwtException.class)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request){
//        ExceptionResponse response= new ExceptionResponse(ex.getMessage(),LocalDateTime.now());
//        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
//    }
}
