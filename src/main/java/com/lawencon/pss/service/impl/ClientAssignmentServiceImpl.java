package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.constant.Roles;
import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentReqDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;
import com.lawencon.pss.model.ClientAssignment;
import com.lawencon.pss.model.User;
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
	public List<ClientAssignmentResDto> getClientById() {
		final var id = principalService.getUserId();
		final Optional<User> loggedIn = userRepository.findById(id);
		final List<ClientAssignmentResDto> response = new ArrayList<>();
		
		String roleCode;
		if(loggedIn.get() != null) {
			roleCode = loggedIn.get().getRole().getRoleCode();
			
			if(Roles.PS.getCode().equals(roleCode)) {
				final var result = clientAssignmentRepository.findByPsId(id);
				
				for (ClientAssignment pair : result) {
					final var client = new ClientAssignmentResDto();
					final var clientUser = pair.getClient();
					final var company = clientUser.getCompany();

					client.setId(clientUser.getId());
					client.setCompany(company.getCompanyName());
					client.setName(clientUser.getFullName());

					if (company.getLogoId() != null) {
//						final var fileContent = company.getLogoId().getFileContent();
//						final var fileExt = company.getLogoId().getFileExt();
//						client.setFileContent(fileContent);
//						client.setFileExt(fileExt);
						client.setFileId(company.getLogoId().getId());
					}

					response.add(client);
				}
				
			}else if(Roles.CL.getCode().equals(roleCode)) {
				final var result = clientAssignmentRepository.findByClientId(id);
				
				if(result.get() != null) {
					final var client = new ClientAssignmentResDto();
					final var pair = result.get();
					final var psUser = pair.getPs();
					final var company = psUser.getCompany();

					client.setId(psUser.getId());
					client.setCompany(company.getCompanyName());
					client.setName(psUser.getFullName());

					if (company.getLogoId() != null) {
//						final var fileContent = company.getLogoId().getFileContent();
//						final var fileExt = company.getLogoId().getFileExt();
//						client.setFileContent(fileContent);
//						client.setFileExt(fileExt);
						client.setFileId(company.getLogoId().getId());
					}
					response.add(client);
				}
				
			}
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
