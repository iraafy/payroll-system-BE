//package com.lawencon.pss.model;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "tb_r_payrolls")
//public class Payroll extends BaseModel{
//
//    @ManyToOne
//    @JoinColumn(name = "client_id", nullable = false)
//    private User clientId;
//
//    private String title;
//
//    @Column(name = "scheduled_date", nullable = false)
//    private LocalDateTime scheduleDate;
//}
