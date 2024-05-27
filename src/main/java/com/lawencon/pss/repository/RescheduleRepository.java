package com.lawencon.pss.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Reschedule;

@Repository
public interface RescheduleRepository extends JpaRepository<Reschedule, String> {

	Optional<Reschedule> findById(String id);

	List<Reschedule> findAll();

	Optional<Reschedule> findByPayrollDetailIdId(String id);

	@Query("SELECT trr FROM Reschedule trr " 
			+"INNER JOIN trr.payrollDetailId trpd " 
			+"INNER JOIN trpd.payroll trp " 
			+"WHERE trp.id = :payroll " 
			+"AND trr.createdAt = " 
			+"   (SELECT MAX(trr2.createdAt) " 
			+"   FROM Reschedule trr2 " 
			+"   INNER JOIN trr2.payrollDetailId trpd2 " 
			+"   INNER JOIN trpd2.payroll trp2 " 
			+"   WHERE trp2.id = :payroll " 
			+"   AND trr2.payrollDetailId.id = trr.payrollDetailId.id"
			+"   AND trr2.isApprove != true) ")
	List<Reschedule> findBypayrollDetailIdId(@Param("payroll") String payrollId);

	List<Reschedule> findBypayrollDetailIdPayrollId(String id);

	Optional<Reschedule> findFirstBypayrollDetailIdIdOrderByCreatedAtDesc(String id);

}
