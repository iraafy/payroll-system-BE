package com.lawencon.pss.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class BaseModel {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_by")
	private Long updatedBy;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "ver", nullable = false)
	@Version
	private Long ver;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
}
