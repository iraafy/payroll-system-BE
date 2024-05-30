package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.clientassignment.AllAssignmentResDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentReqDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;

public interface ClientAssignmentService {
	
	List<ClientAssignmentResDto> getClientById();
	InsertResDto assignPs(ClientAssignmentReqDto request);
	List<AllAssignmentResDto> getAllAssignment();

}
