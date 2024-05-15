package com.lawencon.pss.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResDto {

    private String token;
	private String fullName;
	private String id;
	private String path;
	private String roleCode;
	private String companyName;

}
