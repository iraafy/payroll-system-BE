package com.lawencon.pss.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.payroll.PayrollDetailReqDto;
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;
import com.lawencon.pss.model.Payroll;
import com.lawencon.pss.model.PayrollDetail;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.CompanyRepository;
import com.lawencon.pss.repository.PayrollRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.PayrollsService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollsService {

	private final PayrollRepository payrollRepository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;
	
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
	public PayrollResDto getPayRollById(String id) {
		final var payrollModel = payrollRepository.findById(id);
		final var payroll = payrollModel.get();

		final var payrollDto = new PayrollResDto();
		payrollDto.setId(payroll.getId());
		if(payroll.getScheduleDate() != null) {
			payrollDto.setScheduleDate(payroll.getScheduleDate().toString());			
		}
		payrollDto.setTitle(payroll.getTitle());

		return payrollDto;
	}

	@Transactional
	@Override
	public InsertResDto createNewPayroll(PayrollReqDto data) {
		final var payrollModel = new Payroll();
		final var user = userRepository.findById(data.getClientId());
		final var res = new InsertResDto();
		if(user.get() != null) {
			final var company = user.get().getCompany();
			
			if(company != null) {
				final Long defaultPaymentDay = company.getDefaultPaymentDay();			
				System.out.print(defaultPaymentDay);
				LocalDate convertedDate = LocalDate.now();
				
				convertedDate = convertedDate.withDayOfMonth(
						convertedDate.getMonth().length( convertedDate.isLeapYear() )
				);
				
				Long lastDate = convertedDate.getLong(ChronoField.DAY_OF_MONTH);
				
				if(lastDate < defaultPaymentDay) {
					String day = convertedDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
					System.out.println(day);
					LocalDateTime dateTime = LocalDateTime.of(convertedDate, LocalTime.MAX.minusSeconds(1));
					
					if(day.equalsIgnoreCase("Sat")) {
						payrollModel.setScheduleDate(dateTime.minusDays(1));
					}else if(day.equalsIgnoreCase("Sun")) {
						payrollModel.setScheduleDate(dateTime.minusDays(2));
						System.out.println(payrollModel.getScheduleDate());
					}else {
						payrollModel.setScheduleDate(LocalDateTime.of(convertedDate, LocalTime.MAX));						
					}
					
				}else {
					LocalDate date = LocalDate.parse(data.getScheduledDate());
					String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
					System.out.println(day);
					LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MAX.minusSeconds(1));
					
					if(day.equalsIgnoreCase("Sat")) {
						payrollModel.setScheduleDate(dateTime.minusDays(1));
					}else if(day.equalsIgnoreCase("Sun")) {
						payrollModel.setScheduleDate(dateTime.minusDays(2));						
						System.out.println(payrollModel.getScheduleDate());						
					}else {
						payrollModel.setScheduleDate(dateTime);											}
				}
			}
			
			final User client = user.get();
			
			payrollModel.setClientId(client);
			payrollModel.setTitle(data.getTitle());
			
			
			payrollModel.setCreatedBy(principalService.getUserId());
			payrollModel.setCreatedAt(LocalDateTime.now());
			payrollModel.setVer(0L);
			payrollModel.setIsActive(true);
			
			final var newPayroll = payrollRepository.save(payrollModel);
			
			res.setId(newPayroll.getId());
			res.setMessage("Payroll " + data.getTitle() + " berhasil terbuat");
		}else {
			res.setMessage("User Not Found !");
		}
		
		return res;
	}

	@Override
	public UpdateResDto setPaymentDate(String id, PayrollReqDto data) {
		final Optional<Payroll> payroll = payrollRepository.findById(id);
		final UpdateResDto updateRes = new UpdateResDto();
		
		if(payroll.get() != null) {
			Payroll pay = payroll.get();
			if(pay.getScheduleDate() == null) {
				pay.setScheduleDate(LocalDateTime.parse(data.getScheduledDate()));
				
				pay = payrollRepository.save(pay);
				updateRes.setVer(pay.getVer());
				updateRes.setMessage("Tanggal Pembayaran Payroll " + pay.getTitle() + " Berhasil Ditetapkan");				
			}else {
				updateRes.setMessage("Tanggal Pembayaran Payroll " + pay.getTitle() + " sudah ditetapkan, silahkan ajukan reschedule untuk mengubah tanggal Pembayaran");
			}
		}else {
			updateRes.setMessage("Tanggal Pembayaran Payroll Gagal Ditetapkan");
		}
		
		return updateRes;
	}

	@Override
	public InsertResDto createPayrollDetails(String id, ArrayList<PayrollDetailReqDto> data) {
		for(PayrollDetailReqDto detail : data) {
			final PayrollDetail newDetail = new PayrollDetail();
		}
		return null;
	}

}
