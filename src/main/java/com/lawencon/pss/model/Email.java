package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_r_email")
@Getter
@Setter
public class Email extends BaseModel {

	@ManyToOne
	@JoinColumn(name = "template_id")
	private EmailTemplate template;

	@Column(name = "content")
	private String content;
}
