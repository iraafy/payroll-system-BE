package com.lawencon.pss.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationResDto {

	private String id;
	private String userId;
	private String contextId;
	private String contextUrl;
	private String notificationContent;
	private String createdAt;
	private Boolean isActive;
}
