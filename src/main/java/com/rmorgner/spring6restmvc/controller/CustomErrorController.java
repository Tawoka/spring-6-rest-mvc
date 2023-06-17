package com.rmorgner.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class CustomErrorController {

  @ExceptionHandler(TransactionSystemException.class)
  ResponseEntity handleJPAViolations(TransactionSystemException exception){
    ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

    if (exception.getCause().getCause() instanceof ConstraintViolationException){
      ConstraintViolationException ve = (ConstraintViolationException) exception.getCause().getCause();

      List<HashMap<String, String>> errors = ve.getConstraintViolations().stream()
          .map(constraintViolation -> {
            HashMap<String, String> errorMap = new HashMap<>();
            errorMap.put(constraintViolation.getPropertyPath().toString(),
                constraintViolation.getMessage());
            return errorMap;
          }).toList();
      return responseEntity.body(errors);
    }

    return responseEntity.build();
  }

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
