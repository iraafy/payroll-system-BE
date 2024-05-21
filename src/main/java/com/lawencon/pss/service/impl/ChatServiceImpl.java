package com.lawencon.pss.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.chat.ChatReqDto;
import com.lawencon.pss.dto.chat.ChatResDto;
import com.lawencon.pss.model.Chat;
import com.lawencon.pss.model.Notification;
import com.lawencon.pss.repository.ChatRepository;
import com.lawencon.pss.repository.NotificationRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.ChatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	
	private final ChatRepository chatRepository;
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	
	@Override
	public ArrayList<ChatResDto> seeChats(Long recipientId) {
		final ArrayList<ChatResDto> chatsRes= new ArrayList<>();
		final ArrayList<Chat> chats = chatRepository.findAll();
		
		for(Chat chat : chats) {
			final ChatResDto chatRes = new ChatResDto();
			chatRes.setMessage(chat.getMessage());
			chatRes.setCreatedAt(chat.getCreatedAt());
			chatRes.setCompanyName("Lawencon");
			chatRes.setUserName("User Dummy");
			chatsRes.add(chatRes);
		}
		
		return chatsRes;
	}
	
	@Override
	public InsertResDto saveChat(ChatReqDto chatReq) {
		Chat chat = new Chat();
		System.out.println("=====================================");
		System.out.println(chatReq.getMessage());
		System.out.println("=====================================");
		chat.setMessage(chatReq.getMessage());
		chat.setRecipientId("b4817bf9-cf54-4170-b148-187c4f889b3c");
		chat.setCreatedBy("b4817bf9-cf54-4170-b148-187c4f889b3c");
		chat = chatRepository.save(chat);
		
		final var notification = new Notification();
		
		notification.setContextId("MESSAGE");
		final var user = userRepository.findById("b4817bf9-cf54-4170-b148-187c4f889b3c").get();
		notification.setUser(user);
		notification.setContextUrl("URL Chat");
		notification.setNotificationContent("Anda memiliki pesan baru");
		notification.setCreatedBy("b4817bf9-cf54-4170-b148-187c4f889b3c");
		
		notificationRepository.save(notification);
		
		final InsertResDto insertRes = new InsertResDto();
		insertRes.setId(chat.getId());
		insertRes.setMessage("Berhasil Menyimpan Data Chat !");
		return insertRes;
	};
}
