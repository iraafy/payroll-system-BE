package com.lawencon.pss.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.ClientAssignment;

@Repository
public interface ClientAssignmentRepository extends JpaRepository<ClientAssignment, String>{

	List<ClientAssignment> findByPsId(String id);
	Optional<ClientAssignment> findByClientId(String id);
	
	@Query(value = 
    		"SELECT * "
    		+ "FROM tb_m_users tmu "
    		+ "LEFT JOIN tb_r_client_assignments trca ON tmu.id = trca.client_id "
    		+ "LEFT JOIN tb_m_users tmu2 ON trca.ps_id = tmu2.id "
    		+ "WHERE tmu.role_id = '8487cf5c-044d-44e9-8e02-e51fd8c8d127';", nativeQuery = true
    		)
    List<ClientAssignment> findClientWithPs();
}
