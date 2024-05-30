package com.lawencon.pss.dto.companies;

import java.io.ByteArrayInputStream;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyResDto {

	private String id;
    private String companyName;
    private Byte payrollDate;
    private String logoBase64Content;
    private ByteArrayInputStream logoContent;

}
