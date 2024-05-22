package com.lawencon.pss.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.lawencon.pss.dto.chat.ChatResDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LiveChatController {
	
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/chat/{roomId}")
    public ChatResDto send(@DestinationVariable String roomId,  ChatResDto message) {
        System.out.println(roomId);
        simpMessagingTemplate.convertAndSend("/send/chat/"+roomId, message);
        return message;
    }
}
