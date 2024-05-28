package com.lawencon.pss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.File;

@Repository
public interface FileRepository extends JpaRepository <File, String> {
	Optional<File> findById(String id);
	
	Optional<File> findByfileName(String fileName);
}
