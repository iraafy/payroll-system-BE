package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "tb_r_chats")
@Entity
public class Chat extends BaseModel {
	@Column(name = "message", nullable = false)
	private String message;
	
	@Column(name = "recipient_id", nullable = false)
	private String recipientId;
}
