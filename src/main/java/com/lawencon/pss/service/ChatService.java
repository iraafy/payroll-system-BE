package com.lawencon.pss.service;

import java.util.ArrayList;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.chat.ChatReqDto;
import com.lawencon.pss.dto.chat.ChatResDto;

public interface ChatService {
	ArrayList<ChatResDto> seeChats(String id);
	InsertResDto saveChat(ChatReqDto chatReq);
	ChatResDto findChat(String id);
}
