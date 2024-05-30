package com.lawencon.pss.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(String id);
    
    List<User> findAll();
    
    List<User> findByRoleRoleCode(String roleCode);
    
    @Query(value = 
    		"SELECT u FROM User AS u"
    		+ " WHERE u.role.roleCode = :roleCode"
    		+ " AND u.id NOT IN"
    		+ " (SELECT ca.client.id"
    		+ " FROM ClientAssignment AS ca)")
    List<User> findAvailableClient(@Param("roleCode") String roleCode);
	
    @Query(value = 
    		"SELECT u.id, u.fullName as clientName, u2.fullName as psName "
    		+ "FROM User AS u "
    		+ "LEFT JOIN ClientAssignment AS ca ON ca.client.id = u.id "
    		+ "LEFT JOIN User AS u2 ON ca.ps.id = u2.id "
    		+ "WHERE u.role.roleCode = :roleCode"
    		)
    List<?> findClientWithPs(@Param("roleCode") String roleCode);
}
