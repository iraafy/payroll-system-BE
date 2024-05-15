package com.lawencon.pss.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Reschedule;


@Repository
public interface RescheduleRepository extends JpaRepository<Reschedule, String>{
    
    Optional<Reschedule> findById(String id);

    List<Reschedule> findAll();
    
    Optional<Reschedule> findByPayrollIdId(String id);

}
