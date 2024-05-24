package com.lawencon.pss.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.lawencon.pss.dto.chat.ChatReqDto;
import com.lawencon.pss.dto.chat.ChatResDto;
import com.lawencon.pss.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LiveChatController {
	
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ChatService chatService;
	
	@MessageMapping("/chat/{roomId}")
    public ChatReqDto send(@DestinationVariable String roomId,  ChatReqDto message) {
		final ChatResDto chatRes = new ChatResDto();
		chatRes.setMessage(message.getMessage());
		chatRes.setUserName(message.getRecipientId());
//		chatService.saveChat(message);
//		final Chat chat = chatService.seeChats(null)
//		chatReq.setMessage(message.get)
        simpMessagingTemplate.convertAndSend("/send/chat/"+roomId, chatRes);
        return message;
    }
}
