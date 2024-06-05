package com.lawencon.pss.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.lawencon.pss.dto.companies.CompanyResDto;
import com.lawencon.pss.dto.report.ReportResDto;
import com.lawencon.pss.repository.PayrollRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.CompanyService;
import com.lawencon.pss.service.PayrollsService;
import com.lawencon.pss.service.ReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final CompanyService companyService;
	private final PayrollsService payrollsService;
	
	private final PayrollRepository payrollRepository;
	private final UserRepository userRepository;

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
	
	@Override
	public JasperPrint exportFinalReport(String id) throws FileNotFoundException, JRException {
		final var payrollModel = payrollRepository.findById(id);
		final var payroll = payrollModel.get();
		
		final var clientModel = userRepository.findById(payroll.getClientId().getId());
		final var client = clientModel.get();
		
		final var payrollDetails = payrollsService.getPayrollDetailsForReport(id);
		
		final var finalReport = new ReportResDto();
		finalReport.setPayrollId(payroll.getId());
		finalReport.setCompanyName(client.getCompany().getCompanyName());
		finalReport.setTitle(payroll.getTitle());
		finalReport.setScheduleDate(payroll.getScheduleDate().toLocalDate().toString());
		
		final File file = ResourceUtils.getFile("classpath:payrollReport.jasper");
		
		
		JRBeanCollectionDataSource detailsData = new JRBeanCollectionDataSource(payrollDetails);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("companyName", finalReport.getCompanyName());
		parameters.put("title", finalReport.getTitle());
		parameters.put("scheduleDate", finalReport.getScheduleDate());
		parameters.put("payrollDetailsDataset", detailsData);
		
		final JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, new JREmptyDataSource());
		
		return jasperPrint;
	}

	
	@Override
	public ReportResDto exportReport(String id) throws FileNotFoundException, JRException {
		final var payrollModel = payrollRepository.findById(id);
		final var payroll = payrollModel.get();
		
		final var clientModel = userRepository.findById(payroll.getClientId().getId());
		final var client = clientModel.get();
		
		final var payrollDetails = payrollsService.getPayrollDetails(id);
		
		final var finalReport = new ReportResDto();
		finalReport.setPayrollId(payroll.getId());
		finalReport.setCompanyName(client.getCompany().getCompanyName());
		finalReport.setTitle(payroll.getTitle());
		
		return finalReport;
	}
}
