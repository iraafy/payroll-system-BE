package com.lawencon.pss.controller;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.file.FileDto;
import com.lawencon.pss.dto.file.FileResDto;
import com.lawencon.pss.dto.ftp.DownloadFtpReqDto;
import com.lawencon.pss.dto.ftp.FtpReqDto;
import com.lawencon.pss.model.File;
import com.lawencon.pss.service.FileService;
import com.lawencon.pss.util.FtpUtil;

@RestController
public class FileController {

	private FileService fileServices;
	public FileController(FileService fileServices) {
		this.fileServices = fileServices;
	}

	@PostMapping("files")
	public ResponseEntity<InsertResDto> addEmployee(@RequestBody FileDto data) {
        final InsertResDto res = fileServices.addNewFile(data);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
	
	@GetMapping("files/{id}")
	public ResponseEntity<FileResDto> getFile(@PathVariable("id") String id) {
	    final var file = fileServices.getById(id);
	    if (file == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	    
	    FileResDto resDto = new FileResDto();
	    resDto.setId(file.getId());
	    resDto.setStoredPath(file.getStoredPath());
	    return new ResponseEntity<>(resDto, HttpStatus.OK);
	}
	
	@PostMapping("ftp")
	public ResponseEntity<InsertResDto> addFile(@RequestBody FtpReqDto request) {
		final var fileBase64 = request.getFileBase64();
		final var remoteLocation = request.getRemoteLocation();
		FtpUtil.sendFile(fileBase64, remoteLocation);
		final InsertResDto response = new InsertResDto();
		response.setMessage("Sucess");
		return new ResponseEntity<InsertResDto>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("ftp")
	public void getFile(@RequestBody DownloadFtpReqDto request) {
		final var remoteFile = request.getRemoteFile();
		final var downloadLocation = request.getDownloadLocation();
		FtpUtil.getFile(remoteFile, downloadLocation);
	}
}
