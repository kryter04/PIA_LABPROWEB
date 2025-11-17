package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    Optional<University> findByName(String name);

    @Query("SELECT u FROM University u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<University> searchByName(@Param("name") String name);

    boolean existsByName(String name);
}
