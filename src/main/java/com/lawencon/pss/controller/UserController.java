package com.lawencon.pss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.user.ChangePasswordReqDto;
import com.lawencon.pss.dto.user.ClientDropdownResDto;
import com.lawencon.pss.dto.user.CreateUserReqDto;
import com.lawencon.pss.dto.user.LoginReqDto;
import com.lawencon.pss.dto.user.LoginResDto;
import com.lawencon.pss.dto.user.UserResDto;
import com.lawencon.pss.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("login")
	public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto request) {
		final Authentication auth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
		authenticationManager.authenticate(auth);
		final var response = userService.login(request);
		return new ResponseEntity<LoginResDto>(response, HttpStatus.OK);
	}
	
	@PostMapping("new")
	public ResponseEntity<InsertResDto> createUser(@RequestBody CreateUserReqDto request) {
		final var response = userService.createUser(request);
		return new ResponseEntity<InsertResDto>(response, HttpStatus.CREATED);
	}
	
	@GetMapping()
	public ResponseEntity<List<UserResDto>> getAllUser() {
		final var response = userService.getAllUser();
		return new ResponseEntity<List<UserResDto>>(response, HttpStatus.OK);
	}
	
	@GetMapping("allPs")
	public ResponseEntity<List<UserResDto>> getAllPs() {
		final var response = userService.getAllPs();
		return new ResponseEntity<List<UserResDto>>(response, HttpStatus.OK);
	}
	
	@GetMapping("allClient")
	public ResponseEntity<List<ClientDropdownResDto>> getAllClient() {
		final var response = userService.getAllClient();
		return new ResponseEntity<List<ClientDropdownResDto>>(response, HttpStatus.OK);
	}
	
	@GetMapping("availableClient")
	public ResponseEntity<List<UserResDto>> getAvailableClient() {
		final var response = userService.getAvailableClients();
		return new ResponseEntity<List<UserResDto>>(response, HttpStatus.OK);
	}
	
	@PatchMapping("changePassword")
	public ResponseEntity<UpdateResDto> changeUserPassword(@RequestBody ChangePasswordReqDto request) {
		final var response = userService.updatePassword(request);
		return new ResponseEntity<UpdateResDto>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResDto> getUserById(@PathVariable String id){
		final var res = userService.getUserById(id);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("{psId}/clients")
	public ResponseEntity<List<UserResDto>> getClientsByPsId(@PathVariable String psId){
		final var res = userService.getClientsByPsId(psId);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
