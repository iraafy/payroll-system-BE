package com.lawencon.pss.service;

import java.io.FileNotFoundException;

import com.lawencon.pss.dto.report.ReportResDto;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {
	
	JasperPrint exportReport() throws FileNotFoundException, JRException;
	
//	JasperPrint exportImage(String id) throws FileNotFoundException, JRException;
	
	JasperPrint exportFinalReport(String id) throws FileNotFoundException, JRException;
	
	
	ReportResDto exportReport(String id) throws FileNotFoundException, JRException;

}
