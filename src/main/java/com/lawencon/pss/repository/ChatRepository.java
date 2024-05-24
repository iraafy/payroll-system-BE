package com.lawencon.pss.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String>{
	
	ArrayList<Chat> findByRecipientId(String RecipientId);
	ArrayList<Chat> findAll();
	
	@Query(value = 
    		"SELECT c FROM Chat AS c "
    		+ "WHERE c.recipientId = :recipientId "
    		+ "OR c.createdBy = :createdBy "
    		+ "ORDER BY c.createdAt ASC")
    ArrayList<Chat> findByRecipientIdOrCreatedBy(@Param("recipientId") String id, @Param("createdBy") String createdBy);
}
