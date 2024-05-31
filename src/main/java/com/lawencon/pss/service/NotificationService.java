package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;
import com.lawencon.pss.dto.notification.NotificationResDto;

public interface NotificationService {
	
	InsertResDto createNotification(NotificationReqDto request);
	List<NotificationResDto> getAllNotification();
	List<NotificationResDto> getTop3Notification();
	UpdateResDto readNotification(String id);
}
