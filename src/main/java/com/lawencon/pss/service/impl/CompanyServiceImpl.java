package com.lawencon.pss.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.companies.CompanyResDto;
import com.lawencon.pss.dto.companies.CreateCompanyReqDto;
import com.lawencon.pss.model.Company;
import com.lawencon.pss.model.File;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.CompanyRepository;
import com.lawencon.pss.repository.FileRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.CompanyService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    
    private final PrincipalService principalService;

    @Override
    public List<CompanyResDto> getAllCompanies() {

        final List<Company> companiesModel = companyRepository.findAll();
        final List<CompanyResDto> companies = new ArrayList<>();
        for (Company company : companiesModel) {
            final var companyDto = new CompanyResDto();
            companyDto.setId(company.getId());
            companyDto.setCompanyName(company.getCompanyName());
            companyDto.setPayrollDate(company.getDefaultPaymentDay());

            companies.add(companyDto);
        }

        return companies;
    }

    @Override
    public CompanyResDto getCompanyById(String id) {
        final var companyModel = companyRepository.findById(id);
        final var company = companyModel.get();

        final var companyDto = new CompanyResDto();
        companyDto.setId(company.getId());
        companyDto.setCompanyName(company.getCompanyName());

        return companyDto;

    }

    @Transactional
    @Override
    public InsertResDto createCompany(CreateCompanyReqDto data) {

        final var companyModel = new Company();
        
        companyModel.setCompanyName(data.getCompanyName());
        companyModel.setDefaultPaymentDay(data.getDefaultPaymentDay());

        final var file = new File();
        file.setFileContent(data.getFileContent());
        file.setFileExt(data.getFileExtension());
        file.setFileName("Company Logo");

        file.setCreatedBy(principalService.getUserId());
        file.setCreatedAt(LocalDateTime.now());
        file.setVer(0L);
        file.setIsActive(true);

        final var newFile = fileRepository.save(file);
        companyModel.setLogoId(newFile);
        
        companyModel.setCreatedBy(principalService.getUserId());
        
        companyModel.setCreatedAt(LocalDateTime.now());
        companyModel.setVer(0L);
        companyModel.setIsActive(true);

        final var newCompany = companyRepository.save(companyModel);

        final var response = new InsertResDto();
        response.setId(newCompany.getId());
        response.setMessage("Company " + data.getCompanyName() + " berhasil terbuat");
        
        return response;

    }

	@Override
	public CompanyResDto getCompanyByClientId(String id) {
		
		final var client = userRepository.findById(id);
		final User clientModel = client.get();
		
		final var company = companyRepository.findById(clientModel.getCompany().getId());
		final Company companyModel = company.get();
		
		final var companyDto = new CompanyResDto();
		companyDto.setCompanyName(companyModel.getCompanyName());
		companyDto.setId(companyModel.getId());
		companyDto.setPayrollDate(companyModel.getDefaultPaymentDay());
		
		return companyDto;
	}
}
