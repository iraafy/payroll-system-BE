package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.lawencon.pss.model.Notification;
import com.lawencon.pss.model.PayrollDetail;
import com.lawencon.pss.model.Reschedule;
import com.lawencon.pss.repository.ClientAssignmentRepository;
import com.lawencon.pss.repository.NotificationRepository;
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
	private final UserRepository userRepository;
	private final PayrollRepository payrollRepository;
	private final ClientAssignmentRepository clientAssignmentRepository;
	private final NotificationRepository notificationRepository;
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
		
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		final var currentRescheduleModel = reschedulesRepository
				.findFirstBypayrollDetailIdIdOrderByCreatedAtDesc(data.getPayrollDetailId());
		
		final var payrollDetailModel = payrollDetailRepository.findById(data.getPayrollDetailId());

		final var response = new InsertResDto();
		Reschedule rescheduleModel = new Reschedule();
		PayrollDetail payrollDetail = payrollDetailModel.get();

		final var clientId = principalService.getUserId();

		if (rescheduleModel != null && payrollDetail != null) {
			final var newDate = LocalDate.parse(data.getNewScheduleDate()).atStartOfDay();
			final var isBefore = newDate.isBefore(payrollDetail.getMaxUploadDate());

			if (isBefore) {
				rescheduleModel.setNewScheduleDate(newDate);
				rescheduleModel.setPayrollDetailId(payrollDetail);
				rescheduleModel.setCreatedBy(clientId);
				rescheduleModel.setIsApprove(false);

				final var newReschedule = reschedulesRepository.save(rescheduleModel);
				final var payroll = payrollRepository
						.findById(rescheduleModel.getPayrollDetailId().getPayroll().getId());
				final var client = userRepository.findById(payroll.get().getClientId().getId());
				final var payrollService = clientAssignmentRepository.findByClientId(client.get().getId());
				final var userEmail = payrollService.get().getPs().getEmail();

				response.setId(newReschedule.getId());
				response.setMessage(payrollDetail.getDescription() + " di reschedule, harap tunggu untuk di approve");

				final var clientAssignment = clientAssignmentRepository.findByClientId(clientId).get();
				final var ps = clientAssignment.getPs();
				final var payrollId = payrollDetail.getPayroll().getId();
				final var url = "/payrolls/" + payrollId + "/reschedule";

				final var notification = new Notification();
				notification.setContextId(newReschedule.getId());
				notification.setContextUrl(url);
				notification.setCreatedBy(clientId);
				notification.setNotificationContent("Pengajuan reschedule activity oleh Client");
				notification.setUser(ps);

				notificationRepository.save(notification);
				
				final var newDateDetail = rescheduleModel.getNewScheduleDate().toLocalDate();

				final Runnable runnable = () -> {
					final var subjectEmail = "Pengajuan Perubahan Jadwal Aktivitas "
							+ payrollDetailModel.get().getDescription();
					Map<String, Object> templateModel = new HashMap<>();
					templateModel.put("fullName", payrollService.get().getPs().getFullName());
					templateModel.put("companyName", payrollService.get().getClient().getCompany().getCompanyName());
					templateModel.put("clientName", payrollService.get().getClient().getFullName());
					templateModel.put("activity", payrollDetailModel.get().getDescription());
					templateModel.put("currentDate", payrollDetailModel.get().getMaxUploadDate().toLocalDate().format(formatter));
					templateModel.put("newDate", newDateDetail.format(formatter));

					try {
						emailService.sendTemplateEmail(userEmail, subjectEmail, "request-reschedule", templateModel);
					} catch (MessagingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				};

				final Thread mailThread = new Thread(runnable);
				mailThread.start();

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
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		final var rescheduleModel = reschedulesRepository.findById(id);
		final var reschedule = rescheduleModel.get();
		final var payroll = payrollRepository.findById(reschedule.getPayrollDetailId().getPayroll().getId());
		final var pd = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
		final var prevDate = pd.get().getMaxUploadDate();
		final var client = userRepository.findById(payroll.get().getClientId().getId());

		reschedule.setIsApprove(true);

		final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
		final var payrollDetailModel = payrollDetail.get();
		payrollDetailModel.setMaxUploadDate(reschedule.getNewScheduleDate());
		payrollDetailRepository.save(payrollDetailModel);

		final var updatedReschedule = reschedulesRepository.save(reschedule);
		final var res = new UpdateResDto();

		final var notification = new Notification();
		final var url = "/payrolls/" + payroll.get().getId();
		notification.setContextId(id);
		notification.setContextUrl(url);
		notification.setCreatedBy(principalService.getUserId());
		notification.setNotificationContent("Pengajuan reschedule untuk aktivitas " + payrollDetailModel.getDescription() + " telah disetujui");
		notification.setUser(client.get());

		notificationRepository.save(notification);

		final Runnable runnable = () -> {
			final var subjectEmail = "Perubahan Jadwal Aktivitas " + payrollDetail.get().getDescription()
					+ " Telah Disetujui.";
			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("activity", payrollDetail.get().getDescription());
			templateModel.put("previousDate", payrollDetail.get().getMaxUploadDate().toLocalDate().format(formatter));
			templateModel.put("currentDate", payrollDetailModel.getMaxUploadDate().toLocalDate().format(formatter));
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
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		final var rescheduleModel = reschedulesRepository.findById(id);
		final Reschedule reschedule = rescheduleModel.get();
		final var requestDate = reschedule.getNewScheduleDate();

		reschedule.setIsApprove(null);

		final var payrollDetail = payrollDetailRepository.findById(reschedule.getPayrollDetailId().getId());
		final var payrollDetailModel = payrollDetail.get();
		final var payroll = payrollDetailModel.getPayroll();
		final var client = payroll.getClientId();

		final var notification = new Notification();
		final var url = "/payrolls/" + payroll.getId();
		notification.setContextId(id);
		notification.setContextUrl(url);
		notification.setCreatedBy(principalService.getUserId());
		notification.setNotificationContent("Pengajuan reschedule untuk aktivitas " + payrollDetailModel.getDescription() + " ditolak");
		notification.setUser(client);

		notificationRepository.save(notification);

		final var updatedReschedule = reschedulesRepository.save(reschedule);
		final var res = new UpdateResDto();
		
		final Runnable runnable = () -> {
			final var subjectEmail = "Perubahan Jadwal Aktivitas " + payrollDetail.get().getDescription()
					+ " Telah Ditolak.";
			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("activity", payrollDetail.get().getDescription());
			templateModel.put("currentDate", payrollDetail.get().getMaxUploadDate().toLocalDate().format(formatter));
			templateModel.put("requestDate", payrollDetailModel.getMaxUploadDate().toLocalDate().format(formatter));
			templateModel.put("fullName", client.getFullName());
			String userEmail = client.getEmail();

			try {
				emailService.sendTemplateEmail(userEmail, subjectEmail, "reject-reschedule", templateModel);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		};

		final Thread mailThread = new Thread(runnable);
		mailThread.start();

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
