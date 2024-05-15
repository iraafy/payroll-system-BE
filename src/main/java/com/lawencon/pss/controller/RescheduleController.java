package com.lawencon.pss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.reschedules.ReschedulesResDto;
import com.lawencon.pss.service.RescheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("reschedules")
@RequiredArgsConstructor
public class RescheduleController {
	
	private final RescheduleService rescheduleService;
	
	@GetMapping()
	public ResponseEntity<List<ReschedulesResDto>> getAllReschedule(){
		
		final var res = rescheduleService.getAllSchedules();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReschedulesResDto> getRecheduleById(@PathVariable String id){
		
		final var res = rescheduleService.getScheduleById(id);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
    
    
}
