package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Optional<Subject> findByName(String name);

    @Query("SELECT s FROM Subject s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Subject> searchByName(@Param("name") String name);

    boolean existsByName(String name);
}
