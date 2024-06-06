package com.lawencon.pss.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
public class ValidateException extends RuntimeException {
	
	private String message;
	private HttpStatus status;
	
	public ValidateException(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

}
