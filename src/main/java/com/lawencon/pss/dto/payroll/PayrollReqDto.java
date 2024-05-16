package com.lawencon.pss.dto.payroll;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayrollReqDto {
	
	private String clientId;
	private String title;
	private String scheduledDate;
	private ArrayList<PayrollDetailReqDto> PayrollRes;
}
