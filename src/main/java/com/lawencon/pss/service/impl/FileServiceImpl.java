package com.lawencon.pss.service.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.file.FileDto;
import com.lawencon.pss.model.File;
import com.lawencon.pss.repository.FileRepository;
import com.lawencon.pss.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@PersistenceContext
	private EntityManager em;
	private final FileRepository fileDao;
	
	public FileServiceImpl(FileRepository fileDao) {
		this.fileDao = fileDao;
	}
	
	@Override
	@Transactional
	public InsertResDto addNewFile(FileDto data) {
		final File file = new File();
		file.setCreatedBy("1");
		System.out.println("=====================================");
		System.out.println(data.getStoredPath());
		System.out.println("=====================================");
		file.setStoredPath(data.getStoredPath());
		
		final InsertResDto responseDto = new InsertResDto();
		final File fileSave = fileDao.save(file);
		
		responseDto.setId(fileSave.getId());
		responseDto.setMessage("File Baerhasil Ditambahkan");
		
		return responseDto;
	}
	
	@Override
	public File getById(String id) {
		final Optional<File> temp = fileDao.findById(id);
		if(temp.isPresent()) {
			return temp.get();
		}
		
		return null;
	}
}
