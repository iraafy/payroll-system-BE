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
import com.lawencon.pss.dto.reschedules.RescheduleReqDto;
import com.lawencon.pss.dto.reschedules.ReschedulesResDto;
import com.lawencon.pss.service.RescheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("reschedules")
@RequiredArgsConstructor
public class RescheduleController {

	private final RescheduleService rescheduleService;

	@GetMapping()
	public ResponseEntity<List<ReschedulesResDto>> getAllReschedule() {

		final var res = rescheduleService.getAllSchedules();

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReschedulesResDto> getRecheduleById(@PathVariable String id) {

		final var res = rescheduleService.getRescheduleById(id);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PostMapping("/new")
	public ResponseEntity<InsertResDto> createNewSchedule(@RequestBody RescheduleReqDto data) {

		final var res = rescheduleService.createReschedule(data);

		if (res.getId() != null) {
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}

		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UpdateResDto> updateStatus(@PathVariable String id) {

		final var res = rescheduleService.updateStatusApproval(id);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/payroll/payrollDetail/{payrollId}")
	public ResponseEntity<List<ReschedulesResDto>> getReschedulesByPayrollDetailId(@PathVariable String payrollId) {
		
		final var res = rescheduleService.getScheduleByPayyrollDetailId(payrollId);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/payroll/{payrollId}")
	public ResponseEntity<List<ReschedulesResDto>> getReschedulesByPayrollId(@PathVariable String payrollId) {
		
		final var res = rescheduleService.getScheduleByPayyrollId(payrollId);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/payroll/{payrollDetailId}/lastSchedule")
	public ResponseEntity<ReschedulesResDto> getLastRescheduleByPayrollDetailId(@PathVariable String payrollDetailId){
		
		final var res = rescheduleService.getLastRescheduleByPayrollDetailId(payrollDetailId);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	

}
