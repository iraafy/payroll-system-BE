package com.lawencon.pss.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;
import com.lawencon.pss.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("notification")
public class NotificationController {

	private final NotificationService notificationService;
	
	@PostMapping()
	public ResponseEntity<InsertResDto> pingClient(@RequestBody NotificationReqDto request) {
		final var response = notificationService.createNotification(request);
		return new ResponseEntity<InsertResDto>(response, HttpStatus.CREATED);
	}
	
}
