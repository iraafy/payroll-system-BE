package com.lawencon.pss.dto.payroll;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayrollResDto {
	
	private String id;
	private String title;
	private String scheduleDate;
	private ArrayList<PayrollDetailResDto> details;
}
