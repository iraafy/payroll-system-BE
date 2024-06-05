package com.lawencon.pss.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserReqDto {

    private String email;
	private String fullName;
	private String roleId;
	private String companyId;
	private String fileContent;
	private String fileExt;
	private String phone;

}
