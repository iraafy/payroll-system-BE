package com.lawencon.pss.dto;

public class UpdateResDto {
	private Integer version;
	private String message;

	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
