package com.lawencon.pss.util;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class TimerUtil {
	
	private TimerUtil() {}
	
	@SuppressWarnings("unchecked")
	public static JobDetail buildJobDetail(@SuppressWarnings("rawtypes") Class jobClass) {
		final JobDataMap jobDataMap = new JobDataMap();
		
		return JobBuilder
				.newJob(jobClass)
				.withIdentity(jobClass.getSimpleName())
				.setJobData(jobDataMap)
				.build();
	}
	
	public static Trigger buildTrigger(@SuppressWarnings("rawtypes") Class jobClass) {
		SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();
		
		return TriggerBuilder
				.newTrigger()
				.withIdentity(jobClass.getSimpleName())
				.withSchedule(builder)
				.startAt(new Date(System.currentTimeMillis()))
				.build();
	}
}
