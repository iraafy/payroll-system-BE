package com.lawencon.pss.service.impl;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.chat.ChatReqDto;
import com.lawencon.pss.dto.chat.ChatResDto;
import com.lawencon.pss.model.Chat;
import com.lawencon.pss.model.Notification;
import com.lawencon.pss.model.User;
import com.lawencon.pss.repository.ChatRepository;
import com.lawencon.pss.repository.NotificationRepository;
import com.lawencon.pss.repository.UserRepository;
import com.lawencon.pss.service.ChatService;
import com.lawencon.pss.service.PrincipalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	
	private final ChatRepository chatRepository;
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final PrincipalService principalService;
	
	@Override
	public ArrayList<ChatResDto> seeChats(String id) {
		final ArrayList<ChatResDto> chatsRes= new ArrayList<>();
		final ArrayList<Chat> chatsIn = chatRepository.findByRecipientIdOrCreatedBy(principalService.getUserId(), id);
		final ArrayList<Chat> chatsOut = chatRepository.findByRecipientIdOrCreatedBy(id, principalService.getUserId());
		
		for(Chat chat : chatsIn) {
			final ChatResDto chatRes = new ChatResDto();
			chatRes.setMessage(chat.getMessage());
			chatRes.setCreatedAt(chat.getCreatedAt());
			chatRes.setUserName(chat.getRecipientId());
			chatsRes.add(chatRes);
		}
		
		for(Chat chat : chatsOut) {
			final ChatResDto chatRes = new ChatResDto();
			chatRes.setMessage(chat.getMessage());
			chatRes.setCreatedAt(chat.getCreatedAt());
			chatRes.setUserName(chat.getRecipientId());
			chatsRes.add(chatRes);
		}
		
		chatsRes.sort((chat1, chat2) -> chat1.getCreatedAt().compareTo(chat2.getCreatedAt()));
	    
	    return chatsRes;
	}
	
	@Override
	@Transactional
	public InsertResDto saveChat(ChatReqDto chatReq) {
		final Optional<User> currentUser = userRepository.findById(chatReq.getSenderId());
		final User user = currentUser.get();
		Chat chat = new Chat();
		chat.setMessage(chatReq.getMessage());
		chat.setRecipientId(chatReq.getRecipientId());
		chat.setCreatedBy(user.getId());
		chat.setCreatedAt(LocalDateTime.now());
		chat = chatRepository.save(chat);
		
		
		final Optional<User> recipient = userRepository.findById(chatReq.getRecipientId());
		Notification notification = new Notification();
		notification.setContextId("NO CONTEXT ID");
		notification.setUser(recipient.get());						
		notification.setContextUrl("NO URL");
		notification.setNotificationContent("Anda memiliki pesan baru");
		notification.setCreatedBy(user.getId());
		notification.setCreatedAt(LocalDateTime.now());
		
		notification = notificationRepository.save(notification);
		
		final InsertResDto insertRes = new InsertResDto();
		insertRes.setId(chat.getId());
		insertRes.setMessage("Berhasil Menyimpan Data Chat !");
		return insertRes;
	}
	
	@Override
	public ChatResDto findChat(String id) {
		final ChatResDto chatRes = new ChatResDto();
		final Optional<Chat> chat = chatRepository.findById(id);
		
		if(chat.get() != null) {
			final Chat selectedChat = chat.get();
			chatRes.setMessage(selectedChat.getMessage());
			chatRes.setUserName(selectedChat.getRecipientId());
			chatRes.setCreatedAt(selectedChat.getCreatedAt());
			return chatRes;
		}
		
		return null;
	};
}
