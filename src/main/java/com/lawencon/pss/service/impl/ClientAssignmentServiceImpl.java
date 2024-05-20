package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.List;

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
			final var companyLogo = company.getLogoId();
			if (companyLogo != null) {
				final var fileContent = company.getLogoId().getFileContent();
				final var fileExt = company.getLogoId().getFileExt();
				
				client.setFileContent(fileContent);
				client.setFileExt(fileExt);
			}
			
			client.setId(clientUser.getId());
			client.setCompany(company.getCompanyName());
			client.setName(clientUser.getFullName());
			
			response.add(client);
		}
		return response;
	}

	@Override
	public InsertResDto assignPs(ClientAssignmentReqDto request) {
		final var response = new InsertResDto();
		final var newAssign = new ClientAssignment();
		final var psId = request.getPsId();
		final var clientId = request.getClientId();
		
		final var createdBy = principalService.getUserId();
		final var ps = userRepository.getReferenceById(psId);
		final var client = userRepository.getReferenceById(clientId);
		
		newAssign.setClient(client);
		newAssign.setPs(ps);
		newAssign.setCreatedBy(createdBy);
		
		final var result = clientAssignmentRepository.save(newAssign);
		response.setId(result.getId());
		response.setMessage("PS berhasil di-assign ke Client");
		
		return response;
	}

}
