package com.lawencon.pss.constant;

public enum Roles {
	
	SA("SAD", "Super Admin"),
	PS("PRS", "Payroll Service"),
	CL("CLN", "Client");
	
	private String code;
	private String name;
	
	Roles(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}

}
