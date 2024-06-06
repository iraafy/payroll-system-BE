package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.reschedules.RescheduleReqDto;
import com.lawencon.pss.dto.reschedules.ReschedulesResDto;
import com.lawencon.pss.model.PayrollDetail;
import com.lawencon.pss.model.Reschedule;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.repository.PayrollDetailRepository;
import com.lawencon.pss.repository.PayrollRepository;
import com.lawencon.pss.repository.RescheduleRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.PrincipalService;
import com.lawencon.pss.service.RescheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RescheduleServiceImpl implements RescheduleService {

	private final RescheduleRepository reschedulesRepository;
	private final PayrollDetailRepository payrollDetailRepository;
	private final EmailService emailService;
	private final ClientAssignmentRepository clientAssignment;
	private final UserRepository userRepository;
	private final PayrollRepository payrollRepository;

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
	    final var currentRescheduleModel = reschedulesRepository
	            .findFirstBypayrollDetailIdIdOrderByCreatedAtDesc(data.getPayrollDetailId());

	    final var response = new InsertResDto();
	    Reschedule rescheduleModel = null;
	    PayrollDetail payrollDetail = null;

	    if (!currentRescheduleModel.isEmpty()) {
	        final var currentReschedule = currentRescheduleModel.get();
	        if (currentReschedule.getIsApprove() == null || currentReschedule.getIsApprove() == false) {
	            rescheduleModel = new Reschedule();
	            final var payrollDetailModel = payrollDetailRepository.findById(data.getPayrollDetailId());
	            payrollDetail = payrollDetailModel.get();
	        }
	    } else {
	        rescheduleModel = new Reschedule();
	        final var payrollDetailModel = payrollDetailRepository.findById(data.getPayrollDetailId());
	        payrollDetail = payrollDetailModel.get();
	    }

	    if (rescheduleModel != null && payrollDetail != null) {
	        final var newDate = LocalDate.parse(data.getNewScheduleDate()).atStartOfDay();
	        final var isBefore = newDate.isBefore(payrollDetail.getMaxUploadDate());

	        if (isBefore) {
	            rescheduleModel.setNewScheduleDate(newDate);
	            rescheduleModel.setPayrollDetailId(payrollDetail);
	            rescheduleModel.setCreatedBy(principalService.getUserId());
	            rescheduleModel.setIsApprove(false);
	            rescheduleModel.setCreatedAt(LocalDateTime.now());
	            rescheduleModel.setVer(0L);
	            rescheduleModel.setIsActive(true);

	            final var newReschedule = reschedulesRepository.save(rescheduleModel);

	            response.setId(newReschedule.getId());
	            response.setMessage(payrollDetail.getDescription() + " di reschedule, harap tunggu untuk di approve");

	            return response;
	        }
	    }

	    response.setId(null);
	    response.setMessage("Maaf aktivitas hanya bisa di reschedule di tanggal sebelumnya.");
	    return response;
	}


	@Transactional
	@Override
	public UpdateResDto acceptStatusApproval(String id) {

		final var rescheduleModel = reschedulesRepository.findById(id);
		final var reschedule = rescheduleModel.get();
		final var payroll = payrollRepository.findById(reschedule.getPayrollDetailId().getPayroll().getId());
		final var client = userRepository.findById(payroll.get().getClientId().getId());

		reschedule.setIsApprove(true);

		final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
		final var payrollDetailModel = payrollDetail.get();
		payrollDetailModel.setMaxUploadDate(reschedule.getNewScheduleDate());
		payrollDetailRepository.save(payrollDetailModel);

		final var updatedReschedule = reschedulesRepository.save(reschedule);
		final var res = new UpdateResDto();

		final Runnable runnable = () -> {
			final var subjectEmail = "Perubahan Jadwal Aktivitas " + payrollDetail.get().getDescription()
					+ " Telah Disetujui.";
			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("activity", payrollDetail.get().getDescription());
			templateModel.put("previousDate", payrollDetail.get().getMaxUploadDate());
			templateModel.put("currentDate", payrollDetailModel.getMaxUploadDate());
			templateModel.put("fullName", client.get().getFullName());
			String userEmail = client.get().getEmail();

			try {
				emailService.sendTemplateEmail(userEmail, subjectEmail, "approve-reschedule", templateModel);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		final Thread mailThread = new Thread(runnable);
		mailThread.start();

		res.setVer(updatedReschedule.getVer());
		res.setMessage("Permintaan untuk pemindahan tanggal aktivitas berhasil disetujui");

		return res;
	}

	@Override
	@Transactional
	public UpdateResDto rejectStatusApproval(String id) {

		final var rescheduleModel = reschedulesRepository.findById(id);
		final Reschedule reschedule = rescheduleModel.get();

		reschedule.setIsApprove(null);

		final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
		final var payrollDetailModel = payrollDetail.get();
		payrollDetailModel.setMaxUploadDate(reschedule.getNewScheduleDate());
		payrollDetailRepository.save(payrollDetailModel);

		final var updatedReschedule = reschedulesRepository.save(reschedule);
		final var res = new UpdateResDto();

		res.setVer(updatedReschedule.getVer());
		res.setMessage("Permintaan untuk pemindahan tanggal aktivitas berhasil ditolak");

		return res;
	}

	@Override
	public List<ReschedulesResDto> getScheduleByPayyrollDetailId(String id) {

		final List<Reschedule> schedulesModel = reschedulesRepository.findBypayrollDetailIdId(id);
		final List<ReschedulesResDto> schedulesDto = new ArrayList<>();

		for (Reschedule reschedule : schedulesModel) {
			final var scheduleDto = new ReschedulesResDto();

			scheduleDto.setId(reschedule.getId());
			scheduleDto.setNewScheduleDate(reschedule.getNewScheduleDate().toString());

			final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
			final PayrollDetail payrollDetailModel = payrollDetail.get();

			scheduleDto.setPayrollDetailDescription(payrollDetailModel.getDescription());
			scheduleDto.setOldScheduleDate(payrollDetailModel.getMaxUploadDate().toString());
			scheduleDto.setPayrollDetailId(payrollDetailModel.getId());
			scheduleDto.setIsApproved(reschedule.getIsApprove());

			schedulesDto.add(scheduleDto);

		}

		return schedulesDto;
	}

	@Override
	public List<ReschedulesResDto> getScheduleByPayyrollId(String payrollId) {
		final List<Reschedule> schedulesModel = reschedulesRepository.findBypayrollDetailIdPayrollId(payrollId);
		final List<ReschedulesResDto> schedulesDto = new ArrayList<>();

		for (Reschedule reschedule : schedulesModel) {
			final var scheduleDto = new ReschedulesResDto();

			scheduleDto.setId(reschedule.getId());
			scheduleDto.setNewScheduleDate(reschedule.getNewScheduleDate().toString());

			final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
			final PayrollDetail payrollDetailModel = payrollDetail.get();

			scheduleDto.setPayrollDetailDescription(payrollDetailModel.getDescription());
			scheduleDto.setOldScheduleDate(payrollDetailModel.getMaxUploadDate().toString());
			scheduleDto.setPayrollDetailId(payrollDetailModel.getId());
			scheduleDto.setIsApproved(reschedule.getIsApprove());

			schedulesDto.add(scheduleDto);

		}

		return schedulesDto;
	}

	@Override
	public ReschedulesResDto getLastRescheduleByPayrollDetailId(String id) {
		final var reschedule = reschedulesRepository.findFirstBypayrollDetailIdIdOrderByCreatedAtDesc(id);

		if (reschedule.isPresent()) {
			final var rescheduleModel = reschedule.get();
			final var scheduleDto = new ReschedulesResDto();
			scheduleDto.setId(rescheduleModel.getId());
			scheduleDto.setNewScheduleDate(rescheduleModel.getNewScheduleDate().toString());
			scheduleDto.setPayrollDetailId(rescheduleModel.getPayrollDetailId().getId());
			scheduleDto.setIsApproved(rescheduleModel.getIsApprove());

			final var payrollDetailModel = payrollDetailRepository
					.findById(rescheduleModel.getPayrollDetailId().getId());
			final var payrollDetail = payrollDetailModel.get();

			scheduleDto.setOldScheduleDate(payrollDetail.getMaxUploadDate().toString());
			scheduleDto.setPayrollDetailDescription(payrollDetail.getDescription());

			return scheduleDto;
		} else {
			return null;
		}

	}

}
