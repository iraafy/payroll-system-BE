package com.lawencon.pss.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, String>{
    
    Optional<Payroll> findById(String id);
    
    List<Payroll> findByClientIdId(String id);

    List<Payroll> findAll();
    
    @Query("SELECT p from Payroll as p "
    		+ "WHERE (LOWER(p.title) LIKE %:value% "
    		+ "OR UPPER(p.title) LIKE %:value% "
    		+ "OR p.title LIKE %:value% )"
    		+ "AND p.clientId.id = :id ")
    List<Payroll> searchPayroll(@Param("id") String id, @Param("value") String value);
    
    @Query("SELECT p from Payroll as p "
    		+ "WHERE p.clientId.id "
    		+ "IN "
    		+ "(SELECT ca.client.id FROM ClientAssignment as ca "
    		+ "WHERE ca.ps.id = :id) ")
    List<Payroll> findByPsId(@Param("id") String id);
    
    
}
