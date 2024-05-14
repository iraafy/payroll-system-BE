package com.lawencon.pss.service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.file.FileDto;
import com.lawencon.pss.model.File;

public interface FileService {
	InsertResDto addNewFile(FileDto newFile);
	File getById(String id);
}
