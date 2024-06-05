package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.lawencon.pss.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		emailSender.send(message);
    }
    
    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> templateModel) throws MessagingException, IOException {
    	 MimeMessage message = emailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

         Context context = new Context();
         context.setVariables(templateModel);         

         String htmlBody = templateEngine.process(templateName, context);
         System.out.println("Processed HTML Body: " + htmlBody);

         helper.setTo(to);
         helper.setSubject(subject);
         helper.setText(htmlBody, true);

         emailSender.send(message);
    }
    
}
