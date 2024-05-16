package com.lawencon.pss.dto.payroll;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollDetailReqDto {
	private String id;
	private String description;
	private String filePath;
	private LocalDateTime maxUploadDate; 
}
