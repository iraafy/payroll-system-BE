package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;
import com.lawencon.pss.model.ClientAssignment;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.service.ClientAssignmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAssignmentServiceImpl implements ClientAssignmentService {

	private final ClientAssignmentRepository clientAssignmentRepository;
	
	@Override
	public List<ClientAssignmentResDto> getClientByPsId(String id) {
		final List<ClientAssignmentResDto> response = new ArrayList<>();
		final var result = clientAssignmentRepository.findByPsId(id);
		for (ClientAssignment pair : result) {
			final var client = new ClientAssignmentResDto();
			final var clientUser = pair.getClient();
			client.setId(clientUser.getId());
			client.setCompany(clientUser.getCompany().getCompanyName());
			client.setName(clientUser.getFullName());
			
			response.add(client);
		}
		return response;
	}

}
