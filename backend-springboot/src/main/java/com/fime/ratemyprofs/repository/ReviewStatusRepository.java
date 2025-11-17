package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewStatusRepository extends JpaRepository<ReviewStatus, Integer> {

    Optional<ReviewStatus> findByStatusName(String statusName);

    boolean existsByStatusName(String statusName);
}
