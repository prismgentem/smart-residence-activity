package org.example.mainservice.repository;

import org.example.mainservice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    List<Admin> findAllByResidenceId(UUID id);

    Optional<Admin> findByEmail(String email);
}
