package com.lawencon.pss.dto.reschedules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReschedulesResDto {

    private String id;
    private String payrollDetailDescription;
    private String oldScheduleDate;
    private String newScheduleDate;
    private String payrollDetailId;
    private Boolean isApproved;
    
}
