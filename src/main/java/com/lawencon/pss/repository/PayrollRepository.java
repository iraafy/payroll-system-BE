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
    		+ "WHERE p.title LIKE %:value% "
    		+ "OR p.scheduleDate LIKE %:value%")
    List<Payroll> searchPayroll(@Param("value") String value);
}
