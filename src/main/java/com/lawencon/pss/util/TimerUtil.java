package com.lawencon.pss.util;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.lawencon.pss.job.ReminderData;

public class TimerUtil {
	
	private TimerUtil() {}
	
	@SuppressWarnings("unchecked")
	public static JobDetail buildJobDetail(@SuppressWarnings("rawtypes") Class jobClass, ReminderData reminder) {
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(jobClass.getSimpleName(), reminder);
		
		return JobBuilder
				.newJob(jobClass)
				.withIdentity(jobClass.getSimpleName())
				.setJobData(jobDataMap)
				.build();
	}
	
	public static Trigger buildTrigger(@SuppressWarnings("rawtypes") Class jobClass, ReminderData reminder) {
		SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5);
		
		return TriggerBuilder
				.newTrigger()
				.withIdentity(jobClass.getSimpleName())
				.withSchedule(builder)
				.startAt(reminder.getDate())
				.build();
	}
}
