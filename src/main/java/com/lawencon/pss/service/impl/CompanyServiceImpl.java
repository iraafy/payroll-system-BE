package com.lawencon.pss.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.companies.CompanyResDto;
import com.lawencon.pss.dto.companies.CreateCompanyReqDto;
import com.lawencon.pss.model.Company;
import com.lawencon.pss.model.File;
import com.lawencon.pss.repository.CompanyRepository;
import com.lawencon.pss.service.CompanyService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
//    private FileRepository fileRepository;

    @Override
    public List<CompanyResDto> getAllCompanies() {

        final List<Company> companiesModel = companyRepository.findAll();
        final List<CompanyResDto> companies = new ArrayList<>();
        for (Company company : companiesModel) {
            final var companyDto = new CompanyResDto();
            companyDto.setId(company.getId());
            companyDto.setCompanyName(company.getCompanyName());

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

//    @Transactional
//    @Override
//    public InsertResDto createCompany(CreateCompanyReqDto data) {
//        final var user = userDao.getUserById(principalService.getUserId());
//
//        // final var uuidCompany = UUID.randomUUID().toString();
//
//        final var companyModel = new Company();
//        // companyModel.setId(uuidCompany);
//        companyModel.setCompanyName(data.getCompanyName());
//
//        final var file = new File();
//        file.setId(UUID.randomUUID().toString());
//        file.setPath(data.getFilePath());
//
//        file.setCreatedBy(user.getId());
//        file.setCreatedAt(LocalDateTime.now());
//        file.setVrsion(0L);
//        file.setIsActive(true);
//
//        fileRepository.saveFile(file);
//        companyModel.setLogoId(file);
//
//        companyModel.setLogoId(file);
//        companyModel.setCreatedBy(user.getId());
//        companyModel.setCreatedAt(LocalDateTime.now());
//        companyModel.setVrsion(0L);
//        companyModel.setIsActive(true);
//
//        companyRepository.save(companyModel);
//
//        final var response = new InsertResDto();
//        response.setId(uuidCompany);
//        response.setMessage("Company " + data.getCompanyName() + " berhasil terbuat");
//
//    }

}
