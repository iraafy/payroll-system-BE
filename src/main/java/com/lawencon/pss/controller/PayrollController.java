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
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;
import com.lawencon.pss.service.PayrollsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("payrolls")
@RequiredArgsConstructor
public class PayrollController {

	private final PayrollsService payrollsService;

	@GetMapping
	public ResponseEntity<List<PayrollResDto>> getAllPayrolls() {

		final var res = payrollsService.getAllPayRolls();

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PayrollResDto> getPayRollById(@PathVariable String id) {
		
		final var res = payrollsService.getPayRollById(id);
		
		return new ResponseEntity<>(res, HttpStatus.OK);

	}
	
	@PostMapping("/new")
	public ResponseEntity<InsertResDto> createNewPayroll(@RequestBody PayrollReqDto data){
		
		final var res = payrollsService.createNewPayroll(data);
		
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<UpdateResDto> setScheduledDate(@PathVariable String id, @RequestBody PayrollReqDto data) {
		final UpdateResDto res = payrollsService.setPaymentDate(id, data);
		if(res.getVer() != null) {
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}

}