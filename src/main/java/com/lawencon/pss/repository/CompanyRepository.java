package com.lawencon.pss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    Optional<Company> findById(Long id);

}
