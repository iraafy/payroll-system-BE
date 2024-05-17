package com.lawencon.pss.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationReqDto {
	
	private String notificationContent;
	
	private String contextUrl;
	
	private String contextId;

}
