package com.lawencon.pss.job;

import java.util.Date;

import com.lawencon.pss.dto.notification.NotificationReqDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReminderData {

	private Date date;
	private String message;
	private String fullName;
	private String email;
	private String activityLink;
	private NotificationReqDto request;
	
}
