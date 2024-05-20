package com.lawencon.pss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.role.RoleResDto;
import com.lawencon.pss.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {
	
	private final UserService userService;
	
	@GetMapping()
	public ResponseEntity<List<RoleResDto>> allRoles() {
		final List<RoleResDto> roles = userService.getAllRoles();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
	
}
