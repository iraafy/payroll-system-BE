package com.lawencon.pss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>{

	List<Notification> findAll();
	List<Notification> findByUserId(String id);
}
