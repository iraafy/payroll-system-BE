package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.reschedules.RescheduleReqDto;
import com.lawencon.pss.dto.reschedules.ReschedulesResDto;

public interface RescheduleService {
    
    List<ReschedulesResDto> getAllSchedules();

    ReschedulesResDto getScheduleById(String id);
    
    InsertResDto createReschedule(RescheduleReqDto data);
    
    UpdateResDto updateStatusApproval(String id);

    ReschedulesResDto getScheduleByPayyrollId(String payrollId);

}
