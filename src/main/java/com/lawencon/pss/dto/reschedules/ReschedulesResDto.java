package com.lawencon.pss.dto.schedules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulesResDto {

    private String id;
    private String newScheduleDate;
    private String payrollId;
    private Boolean isApproved;
    
}
