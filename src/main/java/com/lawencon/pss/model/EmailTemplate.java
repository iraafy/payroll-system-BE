package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_m_email_templates")
@Getter
@Setter
public class EmailTemplate extends BaseModel {
	
	@Column(name = "email_code")
	private String code;
	
	@Column(name = "email_subject")
	private String subject;
	
	@Column(name = "email_body")
	private String body;

}
