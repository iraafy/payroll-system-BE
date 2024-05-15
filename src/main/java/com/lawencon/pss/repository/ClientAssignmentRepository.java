package com.lawencon.pss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.ClientAssignment;

@Repository
public interface ClientAssignmentRepository extends JpaRepository<ClientAssignment, String>{

	List<ClientAssignment> findByPsId(String id);
	
	
}
