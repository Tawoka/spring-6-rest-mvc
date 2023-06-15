package com.rmorgner.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class CustomErrorController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){

    List<HashMap<String, String>> errorMaps = exception.getFieldErrors()
        .stream()
        .map(fieldError -> {
          HashMap<String, String> errorMap = new HashMap<>();
          errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
          return errorMap;
        }).toList();

    return ResponseEntity.badRequest().body(errorMaps);
  }

}
