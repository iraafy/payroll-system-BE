package com.lawencon.pss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentReqDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;
import com.lawencon.pss.service.ClientAssignmentService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("assign")
@RequiredArgsConstructor
public class ClientAssignmentController {

	private final ClientAssignmentService clientAssignmentService;
	
	@GetMapping()
	public ResponseEntity<List<ClientAssignmentResDto>> getClientPs() {
		final var response = clientAssignmentService.getClientByPsId();
		return new ResponseEntity<List<ClientAssignmentResDto>>(response, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<InsertResDto> assignPs(@RequestBody ClientAssignmentReqDto request) {
		final var response = clientAssignmentService.assignPs(request);
		return new ResponseEntity<InsertResDto>(response, HttpStatus.CREATED);
	}
	
}
