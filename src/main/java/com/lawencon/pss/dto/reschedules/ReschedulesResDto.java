package com.lawencon.pss.dto.reschedules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReschedulesResDto {

    private String id;
    private String newScheduleDate;
    private String payrollId;
    private Boolean isApproved;
    
}
