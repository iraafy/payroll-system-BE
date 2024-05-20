package com.lawencon.pss.controller;

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
import com.lawencon.pss.dto.file.FileReqDto;
import com.lawencon.pss.dto.file.FileResDto;
import com.lawencon.pss.dto.ftp.FtpReqDto;
import com.lawencon.pss.service.FileService;
import com.lawencon.pss.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("files")
public class FileController {

	private final FtpUtil ftpUtil;
	private final FileService fileServices;

	@PostMapping()
	public ResponseEntity<InsertResDto> addEmployee(@RequestBody FileReqDto data) {
        final InsertResDto res = fileServices.addNewFile(data);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
	
	@GetMapping("{id}")
	public ResponseEntity<FileResDto> getFile(@PathVariable("id") String id) {
	    final var file = fileServices.getFileById(id);
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
		final var response = fileServices.addNewFileFtp(request);
		return new ResponseEntity<InsertResDto>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("ftp/{name}")
	public ResponseEntity<?> getFileFromFTP(@PathVariable String name) {
		System.out.println(name);
		final byte[] fileBytes = ftpUtil.getFile("/ftp_server/"+name);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name)
                .body(fileBytes);
	}
}
