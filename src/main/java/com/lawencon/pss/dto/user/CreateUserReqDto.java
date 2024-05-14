package com.lawencon.pss.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserReqDto {


    private String email;
	private String fullName;
	private Long roleId;
	private Long companyId;
	private String path;

}
