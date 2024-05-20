package com.lawencon.pss.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.lawencon.pss.dto.companies.CompanyResDto;
import com.lawencon.pss.service.CompanyService;
import com.lawencon.pss.service.ReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final CompanyService companyService;

	@Override
	public JasperPrint exportReport() throws FileNotFoundException, JRException {
		final List<CompanyResDto> companies = companyService.getAllCompanies();

		File file = ResourceUtils.getFile("classpath:companies.jasper");
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(companies);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "PSS");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, dataSource);

		return jasperPrint;
	}

}
