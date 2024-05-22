package com.lawencon.pss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;
import com.lawencon.pss.dto.notification.NotificationResDto;
import com.lawencon.pss.model.Notification;
import com.lawencon.pss.repository.NotificationRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.NotificationService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final PrincipalService principalService;
	
	@Override
	public InsertResDto createNotification(NotificationReqDto request) {
		final var response = new InsertResDto();
		final Notification notif = new Notification();
		notif.setContextId(request.getContextId());
		notif.setContextUrl(request.getContextUrl());
		notif.setNotificationContent(request.getNotificationContent());
		notif.setUser(userRepository.getReferenceById(request.getUserId()));
		notif.setCreatedBy(principalService.getUserId());
		
		final var result = notificationRepository.save(notif);
		
		response.setId(result.getId());
		response.setMessage("Berhasil membuat pengingat untuk pengguna");
		
		return response;
	}

	@Override
	public List<NotificationResDto> getNotificationById(String id) {
		final List<NotificationResDto> responses = new ArrayList<>();
		final var result = notificationRepository.findByUserId(id);
		
		for (Notification notif : result) {
			final var response = new NotificationResDto();
			response.setUserId(notif.getUser().getId());
			response.setContextId(notif.getContextId());
			response.setContextUrl(notif.getContextUrl());
			response.setNotificationContent(notif.getNotificationContent());
			
			responses.add(response);
		}
		
		return responses;
	}

}
