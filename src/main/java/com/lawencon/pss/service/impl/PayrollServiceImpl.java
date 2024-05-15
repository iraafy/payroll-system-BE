package com.lawencon.pss.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.payroll.PayrollReqDto;
import com.lawencon.pss.dto.payroll.PayrollResDto;
import com.lawencon.pss.model.Payroll;
import com.lawencon.pss.model.User;
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
		System.out.println("======================>" + data.getClientId());
		final var user = userRepository.findById(data.getClientId());
		System.out.println("errorrr" + user);
		final User client = user.get();
		
		payrollModel.setClientId(client);
		payrollModel.setTitle(data.getTitle());
		
		if(data.getScheduledDate() != null) {
			payrollModel.setScheduleDate(LocalDateTime.parse(data.getScheduledDate()));			
		}
		
		payrollModel.setCreatedBy(principalService.getUserId());
		payrollModel.setCreatedAt(LocalDateTime.now());
		payrollModel.setVer(0L);
		payrollModel.setIsActive(true);
		
		final var newPayroll = payrollRepository.save(payrollModel);
		
		final var res = new InsertResDto();
		res.setId(newPayroll.getId());
		res.setMessage("Payroll " + data.getTitle() + " berhasil terbuat");
		
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

}
