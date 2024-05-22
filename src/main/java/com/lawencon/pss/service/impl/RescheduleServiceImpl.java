package com.lawencon.pss.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.reschedules.RescheduleReqDto;
import com.lawencon.pss.dto.reschedules.ReschedulesResDto;
import com.lawencon.pss.model.PayrollDetail;
import com.lawencon.pss.model.Reschedule;
import com.lawencon.pss.repository.PayrollDetailRepository;
import com.lawencon.pss.repository.RescheduleRepository;
import com.lawencon.pss.service.PrincipalService;
import com.lawencon.pss.service.RescheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RescheduleServiceImpl implements RescheduleService {

	private final RescheduleRepository reschedulesRepository;
	private final PayrollDetailRepository payrollDetailRepository;

	private final PrincipalService principalService;

	@Override
	public List<ReschedulesResDto> getAllSchedules() {

		final List<Reschedule> schedulesModel = reschedulesRepository.findAll();
		final List<ReschedulesResDto> schedulesDto = new ArrayList<>();

		for (Reschedule reschedule : schedulesModel) {
			final var scheduleDto = new ReschedulesResDto();

			scheduleDto.setId(reschedule.getId());
			scheduleDto.setNewScheduleDate(reschedule.getNewScheduleDate().toString());
			
			final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
			final PayrollDetail payrollDetailModel = payrollDetail.get();
			
			scheduleDto.setPayrollDetailId(payrollDetailModel.getId());
			scheduleDto.setIsApproved(reschedule.getIsApprove());

			schedulesDto.add(scheduleDto);

		}

		return schedulesDto;

	}

	@Override
	public ReschedulesResDto getRescheduleById(String id) {
		final var reschedule = reschedulesRepository.findById(id);
		final var rescheduleModel = reschedule.get();

		final var scheduleDto = new ReschedulesResDto();
		scheduleDto.setId(rescheduleModel.getId());
		scheduleDto.setNewScheduleDate(rescheduleModel.getNewScheduleDate().toString());
		scheduleDto.setPayrollDetailId(rescheduleModel.getPayrollDetailId().getId());
		scheduleDto.setIsApproved(rescheduleModel.getIsApprove());

		return scheduleDto;
	}

	@Transactional
	@Override
	public InsertResDto createReschedule(RescheduleReqDto data) {

		final var rescheduleModel = new Reschedule();
		final var payrollDetailModel = payrollDetailRepository.findById(data.getPayrollDetailId());
		final PayrollDetail payrollDetail = payrollDetailModel.get();
		
		final var newDate = LocalDate.parse(data.getNewScheduleDate()).atStartOfDay();
	    final var isBefore = newDate.isBefore(payrollDetail.getMaxUploadDate());
	    
	    System.out.println(payrollDetail.getMaxUploadDate());
	    System.out.println(isBefore);

		final var response = new InsertResDto();

		if (isBefore) {
			rescheduleModel.setNewScheduleDate(newDate);
			rescheduleModel.setPayrollDetailId(payrollDetail);

			rescheduleModel.setCreatedBy(principalService.getUserId());

			rescheduleModel.setCreatedAt(LocalDateTime.now());
			rescheduleModel.setVer(0L);
			rescheduleModel.setIsActive(true);

			final var newReschedule = reschedulesRepository.save(rescheduleModel);

			response.setId(newReschedule.getId());
			response.setMessage("Payroll detail dengan id " + newReschedule.getId() + " di reschedule, harap tunggu untuk di approve");

			return response;
		}

		response.setId(null);
		response.setMessage("Maaf payroll hanya bisa di reschedule di tanggal sebelumnya.");
		return response;

	}

	@Transactional
	@Override
	public UpdateResDto updateStatusApproval(String id) {

		final var rescheduleModel = reschedulesRepository.findById(id);
		final Reschedule reschedule = rescheduleModel.get();

		reschedule.setIsApprove(true);
		
		final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
		final var payrollDetailModel = payrollDetail.get();
		payrollDetailModel.setMaxUploadDate(reschedule.getNewScheduleDate());
		payrollDetailRepository.save(payrollDetailModel);
		
		final var updatedReschedule = reschedulesRepository.save(reschedule);
		final var res = new UpdateResDto();

		res.setVer(updatedReschedule.getVer());
		res.setMessage("payroll detail disetujui untuk di reschedule");

		return res;
	}

	@Override
	public ReschedulesResDto getScheduleByPayyrollDetailId(String id) {

		final var reschedule = reschedulesRepository.findByPayrollDetailIdId(id);
		final var rescheduleModel = reschedule.get();

		final var scheduleDto = new ReschedulesResDto();
		scheduleDto.setId(rescheduleModel.getId());
		scheduleDto.setNewScheduleDate(rescheduleModel.getNewScheduleDate().toString());
		scheduleDto.setPayrollDetailId(rescheduleModel.getPayrollDetailId().getId());
		scheduleDto.setIsApproved(rescheduleModel.getIsApprove());

		return scheduleDto; 
	}

}
