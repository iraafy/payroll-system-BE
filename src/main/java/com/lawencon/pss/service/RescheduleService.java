package com.lawencon.pss.service;

import java.util.List;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.UpdateResDto;
import com.lawencon.pss.dto.reschedules.RescheduleReqDto;
import com.lawencon.pss.dto.reschedules.ReschedulesResDto;

public interface RescheduleService {
    
    List<ReschedulesResDto> getAllSchedules();

    ReschedulesResDto getRescheduleById(String id);
    
    InsertResDto createReschedule(RescheduleReqDto data);
    
    UpdateResDto updateStatusApproval(String id);

    ReschedulesResDto getScheduleByPayyrollDetailId(String payrollId);

}
