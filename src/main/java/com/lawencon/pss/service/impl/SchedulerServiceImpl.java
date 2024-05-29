package com.lawencon.pss.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lawencon.pss.job.ReminderJob;
import com.lawencon.pss.service.SchedulerService;
import com.lawencon.pss.util.TimerUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {
	
	private final Scheduler scheduler;
	private final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

	@Override
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@PreDestroy
	public void preDestroy() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void schedule(@SuppressWarnings("rawtypes") Class jobClass) {
		final JobDetail jobDetail = TimerUtil.buildJobDetail(jobClass);
		final Trigger trigger = TimerUtil.buildTrigger(jobClass);
		
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void runReminderJob() {
		this.schedule(ReminderJob.class);
	}

}
