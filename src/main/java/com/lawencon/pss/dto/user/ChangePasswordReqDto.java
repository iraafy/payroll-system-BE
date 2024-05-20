package com.lawencon.pss.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordReqDto {

    private String oldPassword;
	private String newPassword;

}
