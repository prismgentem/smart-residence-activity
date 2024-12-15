package org.example.mainservice.repository;

import org.example.mainservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAllByResidenceId(UUID residenceId);
    Optional<User> findByEmail(String email);
}
