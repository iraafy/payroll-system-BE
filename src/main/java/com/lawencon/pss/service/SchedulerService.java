package com.lawencon.pss.service;

import com.lawencon.pss.job.ReminderData;

public interface SchedulerService {
	
	void init();
	void preDestroy();
	void schedule(@SuppressWarnings("rawtypes") Class jobClass, ReminderData reminder);
	void runReminderJob(ReminderData reminder);

}
