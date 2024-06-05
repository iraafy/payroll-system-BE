package com.lawencon.pss.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lawencon.pss.model.PayrollDetail;

public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, String> {
	List<PayrollDetail> findByPayrollIdOrderByCreatedAtAsc(String id);
	
	List<PayrollDetail> findByPayrollIdAndForClientOrderByCreatedAtAsc(String id, Boolean isTrue);
	
	List<PayrollDetail> findByPayrollClientIdId(String id);
	
	@Query("SELECT pd from PayrollDetail as pd "
    		+ "INNER JOIN pd.payroll as p "
    		+ "WHERE p.clientId.id "
    		+ "IN "
    		+ "(SELECT ca.client.id FROM ClientAssignment as ca "
    		+ "WHERE ca.ps.id = :id) ")
    List<PayrollDetail> findPayrollDetailByPsId(@Param("id") String id);
}
