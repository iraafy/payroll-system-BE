package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;

public interface ClientAssignmentService {
	
	List<ClientAssignmentResDto> getClientByPsId(String id);

}
