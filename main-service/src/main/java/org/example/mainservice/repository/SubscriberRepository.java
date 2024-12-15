package org.example.mainservice.repository;

import org.example.mainservice.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}

