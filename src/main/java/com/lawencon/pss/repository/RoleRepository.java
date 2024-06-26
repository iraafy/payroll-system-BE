package com.lawencon.pss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findById(Long id);
    
    long countById(String id);
}
