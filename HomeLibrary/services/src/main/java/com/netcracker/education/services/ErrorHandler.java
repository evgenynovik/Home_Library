package com.netcracker.education.services;

import com.netcracker.education.services.dto.ExceptionDTO;
import com.netcracker.education.services.exceptions.LogicException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LogicException.class)
    public ResponseEntity<ExceptionDTO> handleLogicException(LogicException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDTO.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST).build());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ExceptionDTO> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionDTO.builder()
                .message(" Cannot read the value " + e.getMessage().toLowerCase())
                .status(HttpStatus.BAD_REQUEST).build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("messages", errors);
        return ResponseEntity.status(status).body(body);
//        return new ResponseEntity<>(body, headers, status);
    }
}
