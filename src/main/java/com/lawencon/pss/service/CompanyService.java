package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.companies.CompanyResDto;
import com.lawencon.pss.dto.companies.CreateCompanyReqDto;

public interface CompanyService {
    
    List<CompanyResDto> getAllCompanies();

    CompanyResDto getCompanyById(String id);

    InsertResDto createCompany(CreateCompanyReqDto data);

	CompanyResDto getCompanyByClientId(String id);
}
