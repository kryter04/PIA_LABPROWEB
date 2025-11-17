package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {
}
