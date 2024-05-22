package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentReqDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;
import com.lawencon.pss.model.ClientAssignment;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.ClientAssignmentService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAssignmentServiceImpl implements ClientAssignmentService {

	private final ClientAssignmentRepository clientAssignmentRepository;
	private final UserRepository userRepository;
	private final PrincipalService principalService;

	@Override
	public List<ClientAssignmentResDto> getClientByPsId() {
		final var id = principalService.getUserId();
		final List<ClientAssignmentResDto> response = new ArrayList<>();
		final var result = clientAssignmentRepository.findByPsId(id);
		for (ClientAssignment pair : result) {
			final var client = new ClientAssignmentResDto();
			final var clientUser = pair.getClient();
			final var company = clientUser.getCompany();

			client.setId(clientUser.getId());
			client.setCompany(company.getCompanyName());
			client.setName(clientUser.getFullName());
			if(clientUser.getFile() != null) {
				client.setFileId(clientUser.getFile().getId());				
			}

			response.add(client);
		}
		return response;
	}

	@Override
	@Transactional
	public InsertResDto assignPs(ClientAssignmentReqDto request) {
		final var response = new InsertResDto();		
		final var psId = request.getPsId();
		final var clientId = request.getClientId();
		final var createdBy = principalService.getUserId(); 
		final var exist = clientAssignmentRepository.findByClientId(clientId);
		
		if (exist.isPresent()) {
			final var clientAssignment = exist.get();
			final var newPs = userRepository.getReferenceById(psId);			
			clientAssignment.setPs(newPs);
			clientAssignment.setUpdatedBy(createdBy);			
			final var result = clientAssignmentRepository.save(clientAssignment);			
			response.setId(result.getId());			
		} else {
			final var newAssign = new ClientAssignment();			
			final var ps = userRepository.getReferenceById(psId);
			final var client = userRepository.getReferenceById(clientId);			
			newAssign.setClient(client);
			newAssign.setPs(ps);
			newAssign.setCreatedBy(createdBy);
			final var result = clientAssignmentRepository.save(newAssign);
			response.setId(result.getId());			
		}
		
		response.setMessage("PS berhasil di-assign ke Client");
		return response;
	}

}
