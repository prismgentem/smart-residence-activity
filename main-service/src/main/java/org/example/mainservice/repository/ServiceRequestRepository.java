package org.example.mainservice.repository;

import org.example.mainservice.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {
    List<ServiceRequest> findAllByResidenceId(UUID id);

    List<ServiceRequest> findAllByUserId(UUID id);
}
