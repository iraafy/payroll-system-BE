package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;


public interface PayrollsService {
	
	List<PayrollResDto> getAllPayRolls();
	
	PayrollResDto getPayRollById(String id);
	
	InsertResDto createNewPayroll(PayrollReqDto data);
    
}
