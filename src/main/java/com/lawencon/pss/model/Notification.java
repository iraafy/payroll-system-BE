package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_m_notifications")
public class Notification extends BaseModel{
	
	@Column(name = "notification_content", nullable = false)
	private String notificationContent;
	
	@Column(name = "context_url")
	private String contextUrl;
	
	@Column(name = "context_id")
	private String contextId;

}
