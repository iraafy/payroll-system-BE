package com.lawencon.pss.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawencon.pss.model.PayrollDetail;

public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, String> {
	ArrayList<PayrollDetail> findByPayrollIdOrderByCreatedAtAsc(String id);
}
