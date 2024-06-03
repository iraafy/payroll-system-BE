package com.lawencon.pss.service;

import java.util.ArrayList;
import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.payroll.PayrollDetailReqDto;
import com.lawencon.pss.dto.payroll.PayrollDetailResDto;
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;
import com.lawencon.pss.dto.payroll.SignatureReqDto;


public interface PayrollsService {
	
	List<PayrollResDto> getAllPayRolls();
	
	List<PayrollResDto> getPayrollByClientId(String id);
	
	PayrollResDto getPayRollById(String id);
	
	InsertResDto createNewPayroll(PayrollReqDto data);
	
	UpdateResDto setPaymentDate(String id, PayrollReqDto data);
	
	InsertResDto createPayrollDetails(String id, PayrollDetailReqDto data);
    
	ArrayList<PayrollDetailResDto> getPayrollDetails(String id);
	
	UpdateResDto psAckPayrollDetails(String id);
	
	UpdateResDto clientAckPayrollDetails(String id);
	
	List<PayrollResDto> searchPayroll(String id, String value);

	List<PayrollDetailResDto> getAllPayrollDetailByClientId(String id);
	
	List<PayrollResDto> getPayrollByPsId();
	
	List<PayrollDetailResDto> getPayrollDetailByPsId();
	
	PayrollDetailResDto getPayrollDetailById(String id);
	
	UpdateResDto setPayrollDetailFile(String detailId, String fileId);
	
	UpdateResDto signPayrollDetail(String detailId, SignatureReqDto signature);
}
