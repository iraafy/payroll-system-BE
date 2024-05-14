package com.lawencon.pss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.user.CreateUserReqDto;
import com.lawencon.pss.dto.user.LoginReqDto;
import com.lawencon.pss.dto.user.LoginResDto;
import com.lawencon.pss.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("login")
	public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto request) {
		final var response = userService.login(request);
		return new ResponseEntity<LoginResDto>(response, HttpStatus.OK);
	}
	
	@PostMapping("new")
	public ResponseEntity<InsertResDto> createUser(@RequestBody CreateUserReqDto request) {
		final var response = userService.createUser(request);
		return new ResponseEntity<InsertResDto>(response, HttpStatus.CREATED);
	}

}
