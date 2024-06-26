package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;
import com.lawencon.pss.dto.notification.NotificationResDto;
import com.lawencon.pss.model.Notification;
import com.lawencon.pss.repository.NotificationRepository;
import com.lawencon.pss.repository.PayrollDetailRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.NotificationService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final PrincipalService principalService;
	private final EmailService emailService;
	private final PayrollDetailRepository payrollDetailRepository;
	
	@Override
	public InsertResDto createNotification(NotificationReqDto request) {
		final var response = new InsertResDto();
		final Notification notif = new Notification();
		final var recipient = userRepository.findById(request.getUserId()).get();
		final var recipientEmail = recipient.getEmail();
//		final var reminder = userRepository.findById(principalService.getUserId()).get();
//		final var reminderName = reminder.getFullName();
		final var fullName = recipient.getFullName();
		final var payroll = payrollDetailRepository.findById(request.getPayrollDetailId());
		notif.setContextId(request.getContextId());
		notif.setContextUrl(request.getContextUrl());
		notif.setNotificationContent(request.getNotificationContent());
		notif.setUser(recipient);
		notif.setCreatedBy("SYSTEM");
		
		final var result = notificationRepository.save(notif);
		
		final Runnable runnable = () -> {
			final var subjectEmail = "Pengingat Unggah Aktivitas";
			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("fullName", fullName);
			templateModel.put("activity", payroll.get().getDescription());
			templateModel.put("url", "http://localhost:4200" + request.getContextUrl());

			try {
				emailService.sendTemplateEmail(recipientEmail, subjectEmail, "email-ping", templateModel);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
        
        final Thread mailThread = new Thread(runnable);
        mailThread.start();
		
		response.setId(result.getId());
		response.setMessage("Berhasil membuat pengingat untuk pengguna");
		
		return response;
	}

	@Override
	public List<NotificationResDto> getAllNotification() {
		final List<NotificationResDto> responses = new ArrayList<>();
		final var userId = principalService.getUserId();
		final var result = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
		
		for (Notification notif : result) {
			final var response = new NotificationResDto();
			response.setId(notif.getId());
			response.setUserId(notif.getUser().getId());
			response.setContextId(notif.getContextId());
			response.setContextUrl(notif.getContextUrl());
			response.setNotificationContent(notif.getNotificationContent());
			response.setCreatedAt(notif.getCreatedAt().toString());
			response.setIsActive(notif.getIsActive());
			
			responses.add(response);
		}
		
		return responses;
	}

	@Override
	public List<NotificationResDto> getTop3Notification() {
		final List<NotificationResDto> responses = new ArrayList<>();
		final var userId = principalService.getUserId();
		final var result = notificationRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
		
		for (Notification notif : result) {
			final var response = new NotificationResDto();
			response.setId(notif.getId());
			response.setUserId(notif.getUser().getId());
			response.setContextId(notif.getContextId());	
			response.setContextUrl(notif.getContextUrl());
			response.setNotificationContent(notif.getNotificationContent());
			response.setCreatedAt(notif.getCreatedAt().toString());
			response.setIsActive(notif.getIsActive());
			
			responses.add(response);
		}
		
		return responses;
	}

	@Override
	public UpdateResDto readNotification(String id) {
		final var notificationModel = notificationRepository.findById(id);
		final var notification = notificationModel.get();
		
		notification.setIsActive(false);
		final var result = notificationRepository.save(notification);
		
		final var res = new UpdateResDto();
		res.setVer(result.getVer());
		res.setMessage("notifikasi sudah di baca");
		
		return res;
	}

	@Override
	public Integer getUnreadCount() {
		final var user = principalService.getUserId();
		final var count = notificationRepository.getUnreadCount(user);
		return count;
	}

}
