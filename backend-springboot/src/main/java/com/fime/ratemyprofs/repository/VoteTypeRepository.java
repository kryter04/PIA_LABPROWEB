package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteTypeRepository extends JpaRepository<VoteType, Integer> {

    Optional<VoteType> findByVoteName(String voteName);

    boolean existsByVoteName(String voteName);
}
