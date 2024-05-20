package com.lawencon.pss.constant;

public enum Roles {
	
	SA("SA001", "Super Admin"),
	PS("PS001", "Payroll Service"),
	CL("CLNT1", "Client");
	
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
