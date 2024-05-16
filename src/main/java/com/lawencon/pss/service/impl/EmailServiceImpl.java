package com.lawencon.pss.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lawencon.pss.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private JavaMailSender emailSender;

	@Override
	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom("aminullah.alfiyanto@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		emailSender.send(message);

	}

}
