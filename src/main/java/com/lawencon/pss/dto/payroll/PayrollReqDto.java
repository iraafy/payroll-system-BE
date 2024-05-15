package com.lawencon.pss.dto.payroll;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayrollReqDto {
	
	private String clientId;
	private String title;
	private String scheduledDate;

}
