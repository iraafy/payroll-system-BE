package com.lawencon.pss.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_r_payroll_details")
public class PayrollDetail extends BaseModel {
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "max_upload_date")
	private LocalDateTime maxUploadDate;
	
	@Column(name = "ps_acknowledge")
	private Boolean psAcknowledge;
	
	@Column(name = "client_acknowledge")
	private Boolean clientAcknowledge;
	
	@Column(name = "for_client")
	private Boolean forClient;
	
	@OneToOne
	@JoinColumn(name = "file_id")
	private File file;
	
	@ManyToOne
	@JoinColumn(name = "payroll_id")
	private Payroll payroll;
}
