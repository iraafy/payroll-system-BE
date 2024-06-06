package com.lawencon.pss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.companies.CompanyResDto;
import com.lawencon.pss.dto.companies.CreateCompanyReqDto;
import com.lawencon.pss.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyResDto>> getAllCompanies() {

        final var res = companyService.getAllCompanies();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<InsertResDto> createCompany(@RequestBody CreateCompanyReqDto data) {
    	
        final var res = companyService.createCompany(data);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResDto> getCompanyById(@PathVariable String id){
    	final var res = companyService.getCompanyById(id);
    	
    	return new ResponseEntity<>(res, HttpStatus.OK);
    }
    
    @GetMapping("client/{id}")
    public ResponseEntity<CompanyResDto> getCompanyByClientId(@PathVariable String id){
    	final var res = companyService.getCompanyByClientId(id);
    	
    	return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
