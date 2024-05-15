package com.lawencon.pss.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_r_reschedules")
public class Reschedule extends BaseModel{

    @Column(name = "new_scheduled_date", nullable = false)
    private LocalDateTime newScheduleDate;

    @ManyToOne
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payrollId;

    @Column(name = "is_approve")
    private Boolean isApprove;

}
