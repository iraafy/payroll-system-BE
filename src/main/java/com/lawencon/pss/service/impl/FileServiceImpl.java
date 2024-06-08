package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.file.FileReqDto;
import com.lawencon.pss.dto.file.FileResDto;
import com.lawencon.pss.dto.ftp.FtpReqDto;
import com.lawencon.pss.model.File;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.repository.FileRepository;
import com.lawencon.pss.repository.PayrollDetailRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.FileService;
import com.lawencon.pss.service.PrincipalService;
import com.lawencon.pss.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;
	private final PayrollDetailRepository payrollDetailRepository;
	private final FtpUtil ftpUtil;
	private final PrincipalService principalService;
	private final UserRepository userRepository;
	private final ClientAssignmentRepository clientAssignmentRepository;
	private final EmailService emailService;
	
	@Override
	@Transactional
	public InsertResDto addNewFile(FileReqDto data) {
		final InsertResDto responseDto = new InsertResDto();
		
		final File file = new File();		
		file.setCreatedBy("1");
		file.setFileContent(data.getFileContent());
		file.setFileExt(data.getFileExt());
		file.setFileName(data.getFileName());
		
		final File fileSave = fileRepository.save(file);
		
		responseDto.setId(fileSave.getId());
		responseDto.setMessage("File Baerhasil Ditambahkan");
		
		return responseDto;
	}
	
	@Override
	public File getFileById(String id) {
		final Optional<File> temp = fileRepository.findById(id);
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}

	@Override
	@Transactional
	public InsertResDto addNewFileFtp(FtpReqDto request) {
		final var response = new InsertResDto();
		
		final var detailId = request.getDetailId();
		final var payroll = payrollDetailRepository.findById(detailId);
		final var fileExt = request.getFileExt();
		final var newDir = "/" + payroll.get().getPayroll().getId();
		final var storedPath = newDir + "/" + detailId + "." + fileExt;
				
		ftpUtil.makeDir(newDir);
		ftpUtil.sendFile(request.getFileContent(), storedPath);
		
		final var file = new File();
		file.setFileName(detailId);
		file.setFileExt(request.getFileExt());
		file.setStoredPath(storedPath);
		file.setCreatedBy(principalService.getUserId());
		
		final var result = fileRepository.save(file);
		final var client = userRepository.findById(payroll.get().getPayroll().getClientId().getId());
		final var payrollService = clientAssignmentRepository.findByClientId(client.get().getId());
		final var userEmail = payrollService.get().getPs().getEmail();
		
		final Runnable runnable = () -> {
			final var subjectEmail = "Klien Telah Mengunggah Dokumen Baru";
			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("fullName", payrollService.get().getPs().getFullName());
			templateModel.put("clientName", client.get().getFullName());
			templateModel.put("companyName", client.get().getCompany().getCompanyName());
			templateModel.put("activity", payroll.get().getDescription());
			templateModel.put("url", "http://localhost:4200/payrolls/" + payroll.get().getPayroll().getId());

			try {
				emailService.sendTemplateEmail(userEmail, subjectEmail, "upload-document", templateModel);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		final Thread mailThread = new Thread(runnable);
		mailThread.start();
		
		response.setId(result.getId());
		response.setMessage("File baru berhasil ditambahkan!");
		
		return response;
	}

	@Override
	public File getFtpFileById(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileResDto getFtpFileByFileName(String fileName) {
		final var fileModel = fileRepository.findByfileName(fileName);
		final var file = fileModel.get();
		
		final var fileDto = new FileResDto();
		fileDto.setId(file.getId());
		fileDto.setStoredPath(file.getStoredPath());
		fileDto.setFileExt(file.getFileExt());
		
		return fileDto;
	}
	
	
}
