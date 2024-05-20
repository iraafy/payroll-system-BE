package com.lawencon.pss.service;

import java.util.ArrayList;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.chat.ChatReqDto;
import com.lawencon.pss.dto.chat.ChatResDto;

public interface ChatService {
	ArrayList<ChatResDto> seeChats(Long recipientId);
	InsertResDto saveChat(ChatReqDto chatReq);
}
