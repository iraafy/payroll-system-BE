package com.lawencon.pss.service;

public interface SchedulerService {
	
	void init();
	void preDestroy();
	void schedule(@SuppressWarnings("rawtypes") Class jobClass);
	void runReminderJob();

}
