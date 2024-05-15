package com.lawencon.pss.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.reschedules.RescheduleReqDto;
import com.lawencon.pss.dto.reschedules.ReschedulesResDto;
import com.lawencon.pss.model.Payroll;
import com.lawencon.pss.model.Reschedule;
import com.lawencon.pss.repository.PayrollRepository;
import com.lawencon.pss.repository.RescheduleRepository;
import com.lawencon.pss.service.PrincipalService;
import com.lawencon.pss.service.RescheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RescheduleServiceImpl implements RescheduleService {

	private final RescheduleRepository reschedulesRepository;
	private final PayrollRepository payrollsRepository;
	
	private final PrincipalService principalService;

	@Override
	public List<ReschedulesResDto> getAllSchedules() {

		final List<Reschedule> schedulesModel = reschedulesRepository.findAll();
		final List<ReschedulesResDto> schedulesDto = new ArrayList<>();

		for (Reschedule reschedule : schedulesModel) {
			final var scheduleDto = new ReschedulesResDto();

			scheduleDto.setId(reschedule.getId());
			scheduleDto.setNewScheduleDate(reschedule.getNewScheduleDate().toString());

			final var payroll = payrollsRepository.findById(reschedule.getId());
			final var payrollModel = payroll.get();

			scheduleDto.setPayrollId(payrollModel.getId());
			scheduleDto.setIsApproved(reschedule.getIsApprove());

			schedulesDto.add(scheduleDto);

		}

		return schedulesDto;

	}

	@Override
	public ReschedulesResDto getScheduleById(String id) {
		final var reschedule = reschedulesRepository.findById(id);
		final var rescheduleModel = reschedule.get();

		final var scheduleDto = new ReschedulesResDto();
		scheduleDto.setId(rescheduleModel.getId());
		scheduleDto.setNewScheduleDate(rescheduleModel.getNewScheduleDate().toString());
		scheduleDto.setPayrollId(rescheduleModel.getPayrollId().getId());
		scheduleDto.setIsApproved(rescheduleModel.getIsApprove());

		return scheduleDto;
	}

	@Transactional
	@Override
	public InsertResDto createReschedule(RescheduleReqDto data) {

		final var rescheduleModel = new Reschedule();
		final var payrollModel = payrollsRepository.findById(data.getPayrollId());
		final Payroll payroll = payrollModel.get();
		final var newDate = LocalDateTime.parse(data.getNewScheduleDate());
		
		final var isBefore = newDate.isBefore(payroll.getScheduleDate());
		
		if(isBefore) {
			rescheduleModel.setNewScheduleDate(newDate);
			rescheduleModel.setPayrollId(payroll);
			
			rescheduleModel.setCreatedBy(principalService.getUserId());
			
			rescheduleModel.setCreatedAt(LocalDateTime.now());
			rescheduleModel.setVer(0L);
			rescheduleModel.setIsActive(true);
			
			final var newReschedule = reschedulesRepository.save(rescheduleModel);
			
			final var response = new InsertResDto();
			response.setId(newReschedule.getId());
			response.setMessage("Payroll dengan id " + newReschedule.getId() + " berhasil di reschedule");

			return response;
		}
		
		return null;

		

	}

}
