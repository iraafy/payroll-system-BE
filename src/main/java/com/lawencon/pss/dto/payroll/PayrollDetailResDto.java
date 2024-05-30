package com.lawencon.pss.dto.payroll;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PayrollDetailResDto {
	private String id;
	private String description;
	private String fileContent;
	private String filePath;
	private LocalDateTime maxUploadDate;
	private String psAcknowledge;
	private String clientAcknowledge;
	private Boolean forClient;
}
