package com.lawencon.pss.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lawencon.pss.dto.ErrorResDto;
import com.lawencon.pss.exception.ValidateException;

@ControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler(ValidateException.class)
	public ResponseEntity<ErrorResDto> handleNotMatchValidation(ValidateException ve) {
		final var response =  new ErrorResDto();
		response.setMessage(ve.getMessage());
		return new ResponseEntity<ErrorResDto>(response, ve.getStatus());
		
	}

}
