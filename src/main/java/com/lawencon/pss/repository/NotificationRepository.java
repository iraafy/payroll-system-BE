package com.lawencon.pss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>{

	List<Notification> findAll();
	List<Notification> findByUserIdOrderByCreatedAtDesc(String id);
	List<Notification> findTop3ByUserIdOrderByCreatedAtDesc(String id);
	@Query("SELECT COUNT(n) FROM Notification as n "
			+ "WHERE n.user.id = :id "
			+ "AND n.isActive = true ")
	Integer getUnreadCount(@Param ("id") String id);
}
