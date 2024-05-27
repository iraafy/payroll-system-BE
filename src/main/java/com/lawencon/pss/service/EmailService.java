package com.lawencon.pss.service;

import java.util.Map;

import javax.mail.MessagingException;

public interface EmailService {
	
    void sendEmail(String to, String subject, String templateName, Map<String, Object> templateModel) throws MessagingException;
    
}
