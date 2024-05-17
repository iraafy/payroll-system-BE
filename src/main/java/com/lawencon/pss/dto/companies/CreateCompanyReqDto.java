package com.lawencon.pss.dto.companies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompanyReqDto {

    private String companyName;
    private String filePath;
    private Byte defaultPaymentDay;
}
