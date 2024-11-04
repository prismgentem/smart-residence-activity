package org.example.smartresidenceactivity.repository;

import org.example.smartresidenceactivity.entity.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, UUID> {

}
