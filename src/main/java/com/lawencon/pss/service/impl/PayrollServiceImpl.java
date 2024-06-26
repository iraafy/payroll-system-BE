package com.lawencon.pss.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lawencon.pss.constant.Roles;
import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.notification.NotificationReqDto;
import com.lawencon.pss.dto.payroll.PayrollDetailReqDto;
import com.lawencon.pss.dto.payroll.PayrollDetailResDto;
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;
import com.lawencon.pss.dto.payroll.SignatureReqDto;
import com.lawencon.pss.dto.report.PayrollDetailsReportResDto;
import com.lawencon.pss.exception.ValidateException;
import com.lawencon.pss.job.ReminderData;
import com.lawencon.pss.model.Notification;
import com.lawencon.pss.model.Payroll;
import com.lawencon.pss.model.PayrollDetail;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.CompanyRepository;
import com.lawencon.pss.repository.FileRepository;
import com.lawencon.pss.repository.NotificationRepository;
import com.lawencon.pss.repository.PayrollDetailRepository;
import com.lawencon.pss.repository.PayrollRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.EmailService;
import com.lawencon.pss.service.PayrollsService;
import com.lawencon.pss.service.PrincipalService;
import com.lawencon.pss.service.SchedulerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollsService {

	private final PayrollRepository payrollRepository;
	private final PayrollDetailRepository payrollDetailRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final CompanyRepository companyRepository;
	private final FileRepository fileRepository;
	private final SchedulerService schedulerService;
	private final EmailService emailService;

	private final PrincipalService principalService;

	@Override
	public List<PayrollResDto> getAllPayRolls() {
		final List<Payroll> payrollModels = payrollRepository.findAll();
		final List<PayrollResDto> payrollsDto = new ArrayList<>();

		for (Payroll payroll : payrollModels) {
			final var payrollDto = new PayrollResDto();
			payrollDto.setId(payroll.getId());
			payrollDto.setScheduleDate(payroll.getScheduleDate().toString());
			payrollDto.setTitle(payroll.getTitle());

			payrollsDto.add(payrollDto);
		}

		return payrollsDto;

	}

	@Override
	public List<PayrollResDto> getPayrollByClientId(String id) {
		final List<PayrollResDto> responses = new ArrayList<>();
		final var result = payrollRepository.findByClientIdId(id);

		for (Payroll payroll : result) {
			final var response = new PayrollResDto();
			response.setId(payroll.getId());
			response.setTitle(payroll.getTitle());
			response.setScheduleDate(payroll.getScheduleDate().toString());
			response.setClientId(id);

			responses.add(response);
		}

		return responses;
	}

	@Override
	public PayrollResDto getPayRollById(String id) {
		final var payrollModel = payrollRepository.findById(id);
		final var payroll = payrollModel.get();

		final var payrollDto = new PayrollResDto();
		payrollDto.setId(payroll.getId());
		if (payroll.getScheduleDate() != null) {
			payrollDto.setScheduleDate(payroll.getScheduleDate().toString());
		}
		payrollDto.setTitle(payroll.getTitle());
		payrollDto.setClientId(payroll.getClientId().getId());

		final var companyModel = companyRepository.findById(payroll.getClientId().getCompany().getId());

		payrollDto.setCompanyName(companyModel.get().getCompanyName());

		return payrollDto;
	}

	@Transactional
	@Override
	public InsertResDto createNewPayroll(PayrollReqDto data) {
		
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		final var payrollModel = new Payroll();
		final var user = userRepository.findById(data.getClientId());
		final var res = new InsertResDto();
		if (user.get() != null) {
			final var company = user.get().getCompany();

			if (company != null) {
				final Byte defaultPaymentDay = company.getDefaultPaymentDay();
				System.out.println(defaultPaymentDay);
				LocalDate convertedDate = LocalDate.parse(data.getScheduledDate());

				convertedDate = convertedDate
						.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));

				Long lastDate = convertedDate.getLong(ChronoField.DAY_OF_MONTH);

				if (lastDate < defaultPaymentDay) {
					String day = convertedDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
					System.out.println(day);
					LocalDateTime dateTime = LocalDateTime.of(convertedDate, LocalTime.MAX.minusSeconds(1));

					if (day.equalsIgnoreCase("Sat")) {
						payrollModel.setScheduleDate(dateTime.minusDays(1));
					} else if (day.equalsIgnoreCase("Sun")) {
						payrollModel.setScheduleDate(dateTime.minusDays(2));
						System.out.println(payrollModel.getScheduleDate());
					} else {
						payrollModel.setScheduleDate(LocalDateTime.of(convertedDate, LocalTime.MIN));
					}

				} else {
					LocalDate date = LocalDate.parse(data.getScheduledDate());
					String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
					System.out.println(day);
					LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MAX.minusSeconds(1));

					if (day.equalsIgnoreCase("Sat")) {
						payrollModel.setScheduleDate(dateTime.minusDays(1));
					} else if (day.equalsIgnoreCase("Sun")) {
						payrollModel.setScheduleDate(dateTime.minusDays(2));
						System.out.println(payrollModel.getScheduleDate());
					} else {
						payrollModel.setScheduleDate(dateTime);
					}
				}
			}

			final User client = user.get();

			payrollModel.setClientId(client);
			payrollModel.setTitle(data.getTitle());

			payrollModel.setCreatedBy(principalService.getUserId());

			final var newPayroll = payrollRepository.save(payrollModel);

			final var notificationModel = new Notification();

			notificationModel.setNotificationContent("Jadwal Payroll untuk Perusahaan anda telah ditetapkan");
			notificationModel.setContextUrl("/payrolls/" + newPayroll.getId());
			notificationModel.setContextId(newPayroll.getId());
			notificationModel.setUser(client);

			notificationModel.setCreatedBy(principalService.getUserId());

			notificationRepository.save(notificationModel);
			
			final Runnable runnable = () -> {
				final var subjectEmail = "Payroll Bulan Ini Telah Ditetapkan.";
				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("url", "http://localhost:4200/payrolls/" + newPayroll.getId());
				templateModel.put("schedule", payrollModel.getScheduleDate().toLocalDate().format(formatter));				
				templateModel.put("fullName", client.getFullName());
				templateModel.put("title", payrollModel.getTitle());
				String userEmail= client.getEmail();				

				try {
					emailService.sendTemplateEmail(userEmail, subjectEmail, "new-payroll", templateModel);
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};

			final Thread mailThread = new Thread(runnable);
			mailThread.start();

			res.setId(newPayroll.getId());
			res.setMessage("Payroll " + data.getTitle() + " berhasil terbuat");
			
			
		} else {
			res.setMessage("User Not Found !");
		}

		return res;
	}

	@Override
	public UpdateResDto setPaymentDate(String id, PayrollReqDto data) {
		final Optional<Payroll> payroll = payrollRepository.findById(id);
		final UpdateResDto updateRes = new UpdateResDto();

		if (payroll.get() != null) {
			Payroll pay = payroll.get();
			if (pay.getScheduleDate() == null) {
				pay.setScheduleDate(LocalDateTime.parse(data.getScheduledDate()));

				pay = payrollRepository.save(pay);
				updateRes.setVer(pay.getVer());
				updateRes.setMessage("Tanggal Pembayaran Payroll " + pay.getTitle() + " Berhasil Ditetapkan");
			} else {
				updateRes.setMessage("Tanggal Pembayaran Payroll " + pay.getTitle()
						+ " sudah ditetapkan, silahkan ajukan reschedule untuk mengubah tanggal Pembayaran");
			}
		} else {
			updateRes.setMessage("Tanggal Pembayaran Payroll Gagal Ditetapkan");
		}

		return updateRes;
	}

	@Override
	public InsertResDto createPayrollDetails(String id, PayrollDetailReqDto data) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		final Optional<Payroll> payroll = payrollRepository.findById(id);
		final InsertResDto res = new InsertResDto();
		
		final var isBefore = data.getMaxUploadDate().isBefore(payroll.get().getScheduleDate().toLocalDate());
		
		if(payroll.get() != null && isBefore) {
			final User user = userRepository.findById(payroll.get().getClientId().getId()).get();
			PayrollDetail newDetail = new PayrollDetail();
			newDetail.setDescription(data.getDescription());
			final String activity = newDetail.getDescription();
			newDetail.setForClient(data.getForClient());
			
			LocalDate date = LocalDate.parse(data.getMaxUploadDate().toString());
			String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			System.out.println(day);
			LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MAX.minusSeconds(1));

			if (day.equalsIgnoreCase("Sat")) {
				newDetail.setMaxUploadDate(dateTime.minusDays(1));
			} else if (day.equalsIgnoreCase("Sun")) {
				newDetail.setMaxUploadDate(dateTime.minusDays(2));
			} else {
				newDetail.setMaxUploadDate(dateTime);
			}
			
			final LocalDate maxUpload = newDetail.getMaxUploadDate().toLocalDate();			
			
			newDetail.setPayroll(payroll.get());
			newDetail.setCreatedBy(principalService.getUserId());

			newDetail = payrollDetailRepository.save(newDetail);
			res.setId(newDetail.getId());
			res.setMessage("Berhasil menambahkan detail aktivitas untuk Payroll " + payroll.get().getTitle());

			final var notificationModel = new Notification();

			notificationModel.setNotificationContent("PS telah menambahkan aktivitas " + activity + " untuk anda");
			notificationModel.setContextUrl("/payrolls/" + payroll.get().getId());
			notificationModel.setContextId(newDetail.getId());
			notificationModel.setUser(user);
			
			notificationModel.setCreatedBy(principalService.getUserId());

			notificationRepository.save(notificationModel);
			
			final var reminder = new ReminderData();
			final var triggerLocalDateTime = LocalDateTime.of(data.getMaxUploadDate().minusDays(2), LocalTime.NOON.minusHours(5));
			final Date triggerDate = Timestamp.valueOf(triggerLocalDateTime);
			
			final var request = new NotificationReqDto();
			request.setContextId("REMINDER");
			request.setContextUrl("/payrolls/" + payroll.get().getId());
			request.setNotificationContent("Anda belum mengisi aktivitas " + activity);
			request.setPayrollDetailId(newDetail.getId());
			request.setUserId(user.getId());
			
			reminder.setActivityLink("http://localhost:4200/payrolls/"+ payroll.get().getId());
			reminder.setDate(triggerDate);
			reminder.setFullName(user.getFullName());
			reminder.setEmail(user.getEmail());
			reminder.setMessage(data.getDescription());
			reminder.setRequest(request);
			
			schedulerService.runReminderJob(reminder);
			
			final Runnable runnable = () -> {
				final var subjectEmail = "Aktivitas Payroll Baru Telah Ditetapkan.";
				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("url", reminder.getActivityLink());
				templateModel.put("schedule", maxUpload.format(formatter));				
				templateModel.put("fullName", user.getFullName());
				templateModel.put("activity", activity);
				String userEmail= user.getEmail();				

				try {
					emailService.sendTemplateEmail(userEmail, subjectEmail, "new-activity", templateModel);
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};

			final Thread mailThread = new Thread(runnable);
			mailThread.start();

			
		}else {
			throw new ValidateException("Deadline tanggal aktivitas tidak boleh melebihi jadwal payroll", HttpStatus.BAD_REQUEST);
		}
		return res;
	}

	@Override
	public ArrayList<PayrollDetailResDto> getPayrollDetails(String id) {
		final var details = payrollDetailRepository.findByPayrollIdOrderByCreatedAtAsc(id);
		final ArrayList<PayrollDetailResDto> resDetails = new ArrayList<>();

		for (PayrollDetail detail : details) {
			final PayrollDetailResDto resDetail = new PayrollDetailResDto();
			resDetail.setId(detail.getId());
			resDetail.setDescription(detail.getDescription());

			if (detail.getFile() != null && detail.getFile().getStoredPath() != null) {
				resDetail.setFilePath(detail.getFile().getStoredPath());
			}

			if (detail.getFile() != null && detail.getFile().getFileContent() != null) {
				resDetail.setFileContent(detail.getFile().getFileContent());
			}

			resDetail.setForClient(detail.getForClient());
			resDetail.setClientAcknowledge(detail.getClientAcknowledge());
			resDetail.setPsAcknowledge(detail.getPsAcknowledge());
			resDetail.setMaxUploadDate(detail.getMaxUploadDate());

			resDetails.add(resDetail);
		}

		return resDetails;
	}

	@Override
	public UpdateResDto psAckPayrollDetails(String id) {
		final Optional<PayrollDetail> payrollDetail = payrollDetailRepository.findById(id);
		final UpdateResDto updateRes = new UpdateResDto();
		if (payrollDetail.get() != null) {
			PayrollDetail detail = payrollDetail.get();
//			detail.setPsAcknowledge(true);

			detail = payrollDetailRepository.save(detail);
			updateRes.setVer(detail.getVer());
			updateRes.setMessage("Berhasil Menandatangani Dokumen");
		}
		return updateRes;
	}

	@Override
	public UpdateResDto clientAckPayrollDetails(String id) {
		final Optional<PayrollDetail> payrollDetail = payrollDetailRepository.findById(id);
		final UpdateResDto updateRes = new UpdateResDto();
		if (payrollDetail.get() != null) {
			PayrollDetail detail = payrollDetail.get();
//			detail.setClientAcknowledge(true);

			detail = payrollDetailRepository.save(detail);
			updateRes.setVer(detail.getVer());
			updateRes.setMessage("Berhasil Menandatangani Dokumen");
		}
		return updateRes;
	}
	
	@Override
	public List<PayrollResDto> searchPayroll(String id, String value) {
		List<Payroll> payrollModels = new ArrayList<>();
		payrollModels.addAll(payrollRepository.searchPayroll(id, value));

		final List<PayrollResDto> payrollsDto = new ArrayList<>();
		for (Payroll payroll : payrollModels) {
			final var payrollDto = new PayrollResDto();
			payrollDto.setId(payroll.getId());
			payrollDto.setScheduleDate(payroll.getScheduleDate().toString());
			payrollDto.setTitle(payroll.getTitle());

			payrollsDto.add(payrollDto);
		}

		return payrollsDto;
	}

	@Override
	public List<PayrollResDto> getPayrollByPsId() {
		final String id = principalService.getUserId();
		final List<Payroll> payrollModels = payrollRepository.findByPsId(id);
		final List<PayrollResDto> payrollsDto = new ArrayList<>();

		for (Payroll payroll : payrollModels) {
			final var payrollDto = new PayrollResDto();
			payrollDto.setId(payroll.getId());
			payrollDto.setScheduleDate(payroll.getScheduleDate().toString());
			payrollDto.setTitle(payroll.getTitle());

			payrollsDto.add(payrollDto);
		}

		return payrollsDto;

	}

	@Override
	public List<PayrollDetailResDto> getPayrollDetailByPsId() {
		final String id = principalService.getUserId();
		final List<PayrollDetail> payrollDetailModels = payrollDetailRepository.findPayrollDetailByPsId(id);
		final List<PayrollDetailResDto> payrollDetailsDto = new ArrayList<>();

		for (PayrollDetail detail : payrollDetailModels) {
			final var resDetail = new PayrollDetailResDto();
			resDetail.setId(detail.getId());
			resDetail.setDescription(detail.getDescription());

			if (detail.getFile() != null && detail.getFile().getStoredPath() != null) {
				resDetail.setFilePath(detail.getFile().getStoredPath());
			}

			if (detail.getFile() != null && detail.getFile().getFileContent() != null) {
				resDetail.setFileContent(detail.getFile().getFileContent());
			}

			resDetail.setForClient(detail.getForClient());
			resDetail.setClientAcknowledge(detail.getClientAcknowledge());
			resDetail.setPsAcknowledge(detail.getPsAcknowledge());
			resDetail.setMaxUploadDate(detail.getMaxUploadDate());

			payrollDetailsDto.add(resDetail);
		}

		return payrollDetailsDto;
	}

	@Override
	public List<PayrollDetailResDto> getAllPayrollDetailByClientId(String id) {
		final var details = payrollDetailRepository.findByPayrollClientIdId(id);
		final List<PayrollDetailResDto> resDetails = new ArrayList<>();

		for (PayrollDetail detail : details) {
			final PayrollDetailResDto resDetail = new PayrollDetailResDto();
			resDetail.setId(detail.getId());
			resDetail.setDescription(detail.getDescription());

			if (detail.getFile() != null && detail.getFile().getStoredPath() != null) {
				resDetail.setFilePath(detail.getFile().getStoredPath());
			}

			if (detail.getFile() != null && detail.getFile().getFileContent() != null) {
				resDetail.setFileContent(detail.getFile().getFileContent());
			}

			resDetail.setForClient(detail.getForClient());
			resDetail.setClientAcknowledge(detail.getClientAcknowledge());
			resDetail.setPsAcknowledge(detail.getPsAcknowledge());
			resDetail.setMaxUploadDate(detail.getMaxUploadDate());

			resDetails.add(resDetail);
		}

		return resDetails;
	}

	@Override
	public PayrollDetailResDto getPayrollDetailById(String id) {
		final var payrollDetailModel = payrollDetailRepository.findById(id);
		final var payrollDetail = payrollDetailModel.get();

		final PayrollDetailResDto resDetail = new PayrollDetailResDto();
		resDetail.setId(payrollDetail.getId());
		resDetail.setDescription(payrollDetail.getDescription());

		if (payrollDetail.getFile() != null && payrollDetail.getFile().getStoredPath() != null) {
			resDetail.setFilePath(payrollDetail.getFile().getStoredPath());
		}

		if (payrollDetail.getFile() != null && payrollDetail.getFile().getFileContent() != null) {
			resDetail.setFileContent(payrollDetail.getFile().getFileContent());
		}

		resDetail.setForClient(payrollDetail.getForClient());
		resDetail.setClientAcknowledge(payrollDetail.getClientAcknowledge());
		resDetail.setPsAcknowledge(payrollDetail.getPsAcknowledge());
		resDetail.setMaxUploadDate(payrollDetail.getMaxUploadDate());

		return resDetail;
	}

	@Override
	public UpdateResDto setPayrollDetailFile(String detailId, String fileId) {
		final var fileModel = fileRepository.findById(fileId);
		final var file = fileModel.get();

		final var payrollDetailModel = payrollDetailRepository.findById(detailId);
		final var payrollDetail = payrollDetailModel.get();

		payrollDetail.setFile(file);

		final var updatedDetail = payrollDetailRepository.save(payrollDetail);
		final var res = new UpdateResDto();
		res.setVer(updatedDetail.getVer());
		res.setMessage("Berhasil set File pada aktivitas " + payrollDetail.getDescription());

		return res;
	}

	@Override
	@Transactional
	public UpdateResDto signPayrollDetail(String detailId, SignatureReqDto signature) {
		final var payrollDetailModel = payrollDetailRepository.findById(detailId);
		var payrollDetail = payrollDetailModel.get();
		
		final var currentUser = principalService.getUserId();
		final var user = userRepository.findById(currentUser);
		
		if(user.get() != null) {
			if(Roles.CL.getCode().equals(user.get().getRole().getRoleCode())) {
				payrollDetail.setClientAcknowledge(signature.getSignatureBase64());
				payrollDetail = payrollDetailRepository.save(payrollDetail);
			}else {
				payrollDetail.setPsAcknowledge(signature.getSignatureBase64());
				payrollDetail = payrollDetailRepository.save(payrollDetail);
			}
		}
		
		payrollDetail = payrollDetailRepository.save(payrollDetail);
		
		System.out.println(payrollDetail.getClientAcknowledge());
		System.out.println(payrollDetail.getClientAcknowledge());
		
		final var res = new UpdateResDto();
		res.setVer(payrollDetail.getVer());
		res.setMessage("Berhasil menandatangani File untuk aktivitas " + payrollDetail.getDescription());
		
		return res;
	}

	@Override
	public List<PayrollDetailsReportResDto> getPayrollDetailsForReport(String id) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		final var details = payrollDetailRepository.findByPayrollIdAndForClientOrderByCreatedAtAsc(id, true);
		final ArrayList<PayrollDetailsReportResDto> resDetails = new ArrayList<>();

		for (PayrollDetail detail : details) {
			final PayrollDetailsReportResDto resDetail = new PayrollDetailsReportResDto();
			resDetail.setId(detail.getId());
			resDetail.setActivityName(detail.getDescription());
			
			if(detail.getUpdatedAt() != null) {
				resDetail.setMaxUpload(detail.getMaxUploadDate().format(formatter));
				resDetail.setUploadedDate(detail.getUpdatedAt().format(formatter));
				
				if(detail.getUpdatedAt().isBefore(detail.getMaxUploadDate())) {
					resDetail.setDescription("Tepat Waktu");
				}else {
					resDetail.setDescription("Terlambat");					
				}
				
			}else {
				resDetail.setMaxUpload("-");
				resDetail.setUploadedDate(detail.getMaxUploadDate().format(formatter));								
				resDetail.setDescription("Dokumen yang dikirim PS");
			}
			
			resDetails.add(resDetail);
		}
		return resDetails;
	}

}
