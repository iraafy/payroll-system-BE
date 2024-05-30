package com.lawencon.pss.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.ClientAssignment;

@Repository
public interface ClientAssignmentRepository extends JpaRepository<ClientAssignment, String>{

	List<ClientAssignment> findByPsId(String id);
	Optional<ClientAssignment> findByClientId(String id);
	
	@Query(value = 
    		"SELECT ca "
    		+ "FROM ClientAssignment AS ca "
    		+ "RIGHT JOIN User AS u ON ca.client.id = u.id "
    		+ "RIGHT JOIN User AS u2 ON ca.ps.id = u2.id "
    		+ "WHERE u.role.roleCode = :roleCode;"
    		)
    List<ClientAssignment> findClientWithPs(@Param("roleCode") String roleCode);
}
