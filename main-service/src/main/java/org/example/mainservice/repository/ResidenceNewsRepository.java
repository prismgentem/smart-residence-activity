package org.example.mainservice.repository;

import org.example.mainservice.entity.ResidenceNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ResidenceNewsRepository extends JpaRepository<ResidenceNews, UUID> {

    List<ResidenceNews> findAllByResidenceId(UUID residenceId);
}
