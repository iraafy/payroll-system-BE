//package com.lawencon.pss.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.lawencon.pss.dto.schedules.SchedulesResDto;
//import com.lawencon.pss.model.Schedule;
//import com.lawencon.pss.repository.PayrollRepository;
//import com.lawencon.pss.repository.ScheduleRepository;
//import com.lawencon.pss.service.SchedulesService;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class ScheduleServiceImpl implements SchedulesService {
//
//    private final ScheduleRepository schedulesRepository;
//
//    private final PayrollRepository payrollsRepository;
//
//    @Override
//    public List<SchedulesResDto> getAllSchedules() {
//
//        final List<Schedule> schedulesModel = schedulesRepository.findAll();
//        final List<SchedulesResDto> schedulesDto = new ArrayList<>();
//
//        for (Schedule schedule : schedulesModel) {
//            final var scheduleDto = new SchedulesResDto();
//
//            scheduleDto.setId(schedule.getId());
//            scheduleDto.setNewScheduleDate(schedule.getNewScheduleDate().toString());
//
//            final var payroll = payrollsRepository.findById(schedule.getId());
//            final var payrollModel = payroll.get();
//
//            scheduleDto.setPayrollId(payrollModel.getId());
//            scheduleDto.setIsApproved(schedule.getIsApprove());
//
//            schedulesDto.add(scheduleDto);
//
//        }
//
//        return schedulesDto;
//
//    }
//
//    @Override
//    public SchedulesResDto getScheduleById(String id) {
//        final var schedule = schedulesRepository.findById(id);
//        final var scheduleModel = schedule.get();
//
//        final var scheduleDto = new SchedulesResDto();
//        scheduleDto.setId(scheduleModel.getId());
//        scheduleDto.setNewScheduleDate(scheduleModel.getNewScheduleDate().toString());
//        scheduleDto.setPayrollId(scheduleModel.getPayrollId().getId());
//        scheduleDto.setIsApproved(scheduleModel.getIsApprove());
//
//        return scheduleDto;
//    }
//
//}
