package com.lawencon.pss.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.pss.dto.InsertResDto;
import com.lawencon.pss.dto.chat.ChatReqDto;
import com.lawencon.pss.dto.chat.ChatResDto;
import com.lawencon.pss.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("chats")
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService chatService;
	
	@GetMapping("")
	public ResponseEntity<ArrayList<ChatResDto>> ChatContents() {
		final ArrayList<ChatResDto> chatsRes = chatService.seeChats();
		return new ResponseEntity<>(chatsRes, HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<InsertResDto> NewContents(@RequestBody ChatReqDto chat) {
		System.out.println(chat.getMessage());
		final InsertResDto insertRes = chatService.saveChat(chat);
		return new ResponseEntity<>(insertRes, HttpStatus.OK);
	}
}
