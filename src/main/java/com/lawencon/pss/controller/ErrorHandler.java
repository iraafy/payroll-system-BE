package com.lawencon.pss.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawencon.pss.exception.ValidateException;

@ControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler(ValidateException.class)
	public ResponseEntity<String> handleNotMatchValidation(ValidateException ve) {
		return new ResponseEntity<String>(ve.getMessage(), ve.getStatus());
		
	}

}
