package com.lawencon.pss.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lawencon.pss.exception.ConvertException;
import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.file.FileReqDto;
import com.lawencon.pss.dto.file.FileResDto;
import com.lawencon.pss.dto.ftp.FtpReqDto;
import com.lawencon.pss.model.File;
import com.lawencon.pss.repository.FileRepository;
import com.lawencon.pss.service.FileService;
import com.lawencon.pss.service.PayrollsService;
import com.lawencon.pss.util.ConverterUtil;
import com.lawencon.pss.util.FtpPojo;
import com.lawencon.pss.util.FtpUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("files")
@CrossOrigin("*")
public class FileController {

	private final FtpUtil ftpUtil;
	private final FileService fileServices;
	private final FileRepository fileRepository;
	private final PayrollsService payrollService;
	private final ConverterUtil convertUtil;
	
	private static final String TMP_DIR = "tmp";
    private static final String SUFFIX = ".pdf";

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
		final var file = fileServices.getFtpFileByFileName(name);
		final var fileName = payrollService.getPayrollDetailById(name).getDescription();
		final byte[] fileBytes = ftpUtil.getFile("/ftp_server/" + file.getStoredPath());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=" + fileName + "." + file.getFileExt()).body(fileBytes);
	}
	
	@GetMapping("ftp/preview/{name}")
	public ResponseEntity<?> getPreviewFromFtp(@PathVariable String name) {
		System.out.println(name);
		
		final var file = fileServices.getFtpFileByFileName(name);
		final byte[] fileBytes = ftpUtil.getFile("/ftp_server/" + file.getStoredPath());
		final var stream = new ByteArrayInputStream(fileBytes);
		String localPath = saveLocal(stream, file.getStoredPath());
		String target = localPath.substring(0, localPath.lastIndexOf(".")) + SUFFIX;
        
		java.io.File pdfFile = null;
        try {
            boolean flag = convertUtil.convert(localPath, target);
            pdfFile = new java.io.File(target);
            
            byte[] bytes = FileUtils.readFileToByteArray(pdfFile);
            if (!flag) {
            	return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName())
                        .body(fileBytes);
            }
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName())
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
            		.body(e.getMessage());
        } finally {
          java.io.File localFile = new java.io.File(localPath);
          if (localFile != null) {
              localFile.delete();
          }
          if (pdfFile != null) {
              pdfFile.delete();
          }
      }
	}

	@GetMapping("file/{id}")
	public ResponseEntity<?> getFileById(@PathVariable("id") String id) {
		File file = null;
		final Optional<File> fileCheck = fileRepository.findById(id);
		
		if (fileCheck.isPresent()) {
			file = fileCheck.get();
		}
		final String fileName = "attachment";
		final byte[] fileBytes = Base64.getDecoder().decode(file.getFileContent());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "." + file.getFileExt())
				.body(fileBytes);
	}
	
	@GetMapping("search/{fileName}")
	public Boolean isExistOnDb(@PathVariable("fileName") String fileName){
		final var file = fileServices.getFtpFileByFileName(fileName);
		if(file != null) {
			return true;
		}else {
			return false;
		}
	}
	
	private String saveLocal(InputStream file, String storedPath) {
        String filename = storedPath;
        if (StringUtils.lastIndexOf(filename, "/") != -1) {
            filename = StringUtils.substringAfterLast(filename, "/");
        }
        if (StringUtils.lastIndexOf(filename, "\\") != -1) {
            filename = StringUtils.substringAfterLast(filename, "\\");
        }
        String localName = TMP_DIR + java.io.File.separator + new Date().getTime() + "_" + filename;
        try {
            var destination = new java.io.File(localName);
            FileUtils.copyToFile(file, destination);
            localName = destination.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(file);
        }
        return localName;
    }
}
