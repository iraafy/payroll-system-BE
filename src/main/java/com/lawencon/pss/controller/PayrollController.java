package com.lawencon.pss.controller;

import java.util.ArrayList;
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
import com.lawencon.pss.dto.payroll.PayrollDetailReqDto;
import com.lawencon.pss.dto.payroll.PayrollDetailResDto;
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;
import com.lawencon.pss.dto.payroll.SignatureReqDto;
import com.lawencon.pss.service.PayrollsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("payrolls")
@RequiredArgsConstructor
public class PayrollController {

	private final PayrollsService payrollsService;

	@GetMapping("/all")
	public ResponseEntity<List<PayrollResDto>> getAllPayrolls() {

		final var res = payrollsService.getAllPayRolls();

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PayrollResDto> getPayRollById(@PathVariable String id) {
		
		final var res = payrollsService.getPayRollById(id);
		
		return new ResponseEntity<>(res, HttpStatus.OK);

	}
	
	@GetMapping
	public ResponseEntity<List<PayrollResDto>> getPayrollByPs() {
		
		final var res = payrollsService.getPayrollByPsId();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<PayrollResDto>> getPayrollByClientId(@PathVariable String clientId) {
		
		final var res = payrollsService.getPayrollByClientId(clientId);
		
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
	
	@PostMapping("{id}/details")
	public ResponseEntity<InsertResDto> getDetails(@PathVariable String id, @RequestBody PayrollDetailReqDto data) {
		final InsertResDto res = payrollsService.createPayrollDetails(id, data);
		
		if(res.getId() != null) {
			return new ResponseEntity<>(res, HttpStatus.OK);			
		}else {
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("{id}/details")
	public ResponseEntity<ArrayList<PayrollDetailResDto>> getDetails(@PathVariable String id) {
		final ArrayList<PayrollDetailResDto> res = payrollsService.getPayrollDetails(id);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PatchMapping("*/details/{id}")
	public ResponseEntity<UpdateResDto> psAcknowledge(@PathVariable String id) {
		final UpdateResDto res = payrollsService.psAckPayrollDetails(id);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("search/{id}/{value}")
	public ResponseEntity<List<PayrollResDto>> searchPayroll(@PathVariable String id, @PathVariable String value) {
		final List<PayrollResDto> payrolls = payrollsService.searchPayroll(id, value);
		return new ResponseEntity<>(payrolls, HttpStatus.OK);
	}
	
	@GetMapping("details/client/{id}")
	public ResponseEntity<List<PayrollDetailResDto>> getAllPayrollDetailByClientId(@PathVariable String id){
		final var res = payrollsService.getAllPayrollDetailByClientId(id);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("payroll-details")
	public ResponseEntity<List<PayrollDetailResDto>> getAllPayrollDetailByPsId(){
		final var res = payrollsService.getPayrollDetailByPsId();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PatchMapping("{detailId}/set-file/{fileId}")
	public ResponseEntity<UpdateResDto> updateFilePayrollDetail(@PathVariable String detailId, @PathVariable String fileId ){
		final var res = payrollsService.setPayrollDetailFile(detailId, fileId);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PatchMapping("{detailId}/sign")
	public ResponseEntity<UpdateResDto> signPayrollDetail(@PathVariable String detailId, @RequestBody SignatureReqDto signature ){
		final var res = payrollsService.signPayrollDetail(detailId, signature);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
