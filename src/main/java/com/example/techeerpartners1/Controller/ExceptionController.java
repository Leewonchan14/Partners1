package com.example.techeerpartners1.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> exceptionHandler(MethodArgumentNotValidException e) {
        HashMap<String, String> map = new HashMap<>();
        e.getAllErrors().forEach(error -> {
            String key = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            map.put(key, message);
        });
        return ResponseEntity.status(400).body(map);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> exceptionHandler2(Exception e) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return ResponseEntity.status(400).body(map);
    }
}
