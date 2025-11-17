package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {

    @Query("SELECT p FROM Professor p LEFT JOIN FETCH p.universities LEFT JOIN FETCH p.subjects WHERE p.professorId = :id")
    Optional<Professor> findByIdWithDetails(@Param("id") Integer id);

    @Query("SELECT DISTINCT p FROM Professor p " +
           "LEFT JOIN p.universities u " +
           "LEFT JOIN p.subjects s " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:universityId IS NULL OR u.universityId = :universityId) " +
           "AND (:subjectId IS NULL OR s.subjectId = :subjectId)")
    Page<Professor> searchProfessors(
        @Param("name") String name,
        @Param("universityId") Integer universityId,
        @Param("subjectId") Integer subjectId,
        Pageable pageable
    );

    @Query("SELECT p FROM Professor p " +
           "JOIN Review r ON r.professor.professorId = p.professorId " +
           "WHERE r.status.statusName = 'Approved' " +
           "GROUP BY p.professorId " +
           "ORDER BY AVG(r.rating) DESC, COUNT(r) DESC")
    List<Professor> findTopRatedProfessors(Pageable pageable);

    @Query("SELECT p FROM Professor p " +
           "JOIN Review r ON r.professor.professorId = p.professorId " +
           "WHERE r.status.statusName = 'Approved' " +
           "GROUP BY p.professorId " +
           "ORDER BY AVG(r.rating) DESC " +
           "LIMIT 1")
    Optional<Professor> findTopRatedProfessor();

    Page<Professor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
