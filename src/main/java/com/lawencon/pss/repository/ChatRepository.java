package com.lawencon.pss.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String>{
	
	Chat findByRecipientId(Long RecipientId);
	ArrayList<Chat> findAll();
}
