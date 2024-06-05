package com.lawencon.pss.dto.companies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompanyReqDto {

    private String companyName;
    private String phone;
    private String address;
    private Byte defaultPaymentDay;
    private String fileContent;
    private String fileExtension;
    
}
