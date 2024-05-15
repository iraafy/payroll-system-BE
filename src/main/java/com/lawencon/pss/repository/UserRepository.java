package com.lawencon.pss.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lawencon.pss.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id); // request Angga
    List<User> findAll();
    List<User> findByRoleRoleCode(String roleCode);
}
