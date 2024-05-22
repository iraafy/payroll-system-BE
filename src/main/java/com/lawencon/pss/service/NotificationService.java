package com.lawencon.pss.service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;

public interface NotificationService {
	
	InsertResDto createNotification(NotificationReqDto request);

}
