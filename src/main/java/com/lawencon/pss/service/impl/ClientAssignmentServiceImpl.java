package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.constant.Roles;
import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.clientassignment.AllAssignmentResDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentReqDto;
import com.lawencon.pss.dto.clientassignment.ClientAssignmentResDto;
import com.lawencon.pss.dto.user.ClientResDto;
import com.lawencon.pss.model.ClientAssignment;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.ClientAssignmentService;
import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientAssignmentServiceImpl implements ClientAssignmentService {

	private final ClientAssignmentRepository clientAssignmentRepository;
	private final UserRepository userRepository;
	private final PrincipalService principalService;
	private final EmailService emailService;	

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
						client.setFileId(company.getLogoId().getId());
					}
					response.add(client);
				}
				
			}
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
		
		final var client = userRepository.findById(request.getClientId());
		final var ps = userRepository.findById(request.getPsId());
		
		final Runnable runnable = () -> {
			final var subjectEmail = "Payroll Service Telah Ditetapkan.";
			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("fullName", client.get().getFullName());
			templateModel.put("ps", ps.get().getFullName());
			final var userEmail = client.get().getEmail();
			try {
				emailService.sendTemplateEmail(userEmail, subjectEmail, "assign-pic", templateModel);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		final Thread mailThread = new Thread(runnable);
		mailThread.start();
		
		response.setMessage("PS berhasil di-assign ke Client");
		return response;
	}

	@Override
	public List<AllAssignmentResDto> getAllAssignment() {
		final List<AllAssignmentResDto> responses = new ArrayList<>();
		final var psList = userRepository.findByRoleRoleCode(Roles.PS.getCode());
		final var assignments = clientAssignmentRepository.findAll();
		
		for (User ps : psList) {
			final var response = new AllAssignmentResDto();
			final List<ClientResDto> clients = new ArrayList<>(); 
			
			response.setPsName(ps.getFullName());
			
			for (ClientAssignment ca : assignments) {
				if (ca.getPs().getFullName().equals(ps.getFullName())) {
					final var client = new ClientResDto();
					final var clientFound = ca.getClient();
					client.setFullName(clientFound.getFullName());
					client.setCompanyName(clientFound.getCompany().getCompanyName());
					if (clientFound.getCompany().getLogoId() != null) {
						client.setCompanyLogo(clientFound.getCompany().getLogoId().getId());
					}
					
					clients.add(client);
				}
			}
			response.setClients(clients);
			responses.add(response);
		}
		return responses;
	}

}
