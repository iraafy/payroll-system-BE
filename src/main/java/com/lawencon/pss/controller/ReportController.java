package com.lawencon.pss.controller;

import java.io.FileNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.service.ReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reports")
@RequiredArgsConstructor
public class ReportController {
	
	private final ReportService reportService;
	
	@GetMapping()
	public ResponseEntity<?> printReport() throws FileNotFoundException, JRException{
		
		final JasperPrint jasperPrint = reportService.exportReport();
		
		byte fileBytes[] = JasperExportManager.exportReportToPdf(jasperPrint);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=companies.pdf");

		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=companies.pdf")
                .body(fileBytes);

	}

}
