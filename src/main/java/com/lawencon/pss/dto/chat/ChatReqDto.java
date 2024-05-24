package com.lawencon.pss.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatReqDto {
	private String message;
	private String recipientId;
	private String senderId;
}
