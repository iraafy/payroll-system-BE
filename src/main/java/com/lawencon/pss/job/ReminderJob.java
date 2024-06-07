package com.lawencon.pss.job;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReminderJob implements Job {

	private final EmailService emailService;
	private final NotificationService notificationService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final Runnable runnable = () -> {
			final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			final ReminderData data = (ReminderData) jobDataMap.get(ReminderJob.class.getSimpleName());
			final String subject = "Pengingat - " + data.getMessage();
			final String email = data.getEmail();
			final Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("fullName", data.getFullName());
			templateModel.put("message", data.getMessage());
			templateModel.put("activityLink", data.getActivityLink());
			
			notificationService.createNotification(data.getRequest());
			
			try {
				emailService.sendTemplateEmail(email, subject, "reminder-email", templateModel);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		
		final Thread mailThread = new Thread(runnable);
		mailThread.start();
	}

	
	
}
