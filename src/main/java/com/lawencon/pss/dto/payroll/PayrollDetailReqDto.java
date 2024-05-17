package com.lawencon.pss.dto.payroll;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollDetailReqDto {
	private String description;
	private String filePath;
	private LocalDate maxUploadDate;
	private Boolean forClient;
}
