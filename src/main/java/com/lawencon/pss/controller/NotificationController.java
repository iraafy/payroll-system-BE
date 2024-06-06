package com.lawencon.pss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;
import com.lawencon.pss.dto.notification.NotificationResDto;
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
	
	@GetMapping()
	public ResponseEntity<List<NotificationResDto>> getAllNotification() {
		final var response = notificationService.getAllNotification();
		return new ResponseEntity<List<NotificationResDto>>(response, HttpStatus.OK);
	}
	
	@GetMapping("top3")
	public ResponseEntity<List<NotificationResDto>> getTop3Notification() {
		final var response = notificationService.getTop3Notification();
		return new ResponseEntity<List<NotificationResDto>>(response, HttpStatus.OK);
	}
	
	@PatchMapping("{id}/read")
	public ResponseEntity<UpdateResDto> readNotification(@PathVariable String id){
		final var response = notificationService.readNotification(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("unread")
	public ResponseEntity<Integer> getUnreadNotificationCount(){
		final var res = notificationService.getUnreadCount();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	
}
