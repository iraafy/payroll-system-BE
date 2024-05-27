package com.lawencon.pss.dto.chat;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResDto {
	private String message;
	private LocalDateTime createdAt;
	private String userName;
}
