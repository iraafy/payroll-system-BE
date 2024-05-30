package com.lawencon.pss.dto.clientassignment;

import java.util.List;

import com.lawencon.pss.dto.user.ClientResDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AllAssignmentResDto {

	private String psName;
	private List<ClientResDto> clients;
}
