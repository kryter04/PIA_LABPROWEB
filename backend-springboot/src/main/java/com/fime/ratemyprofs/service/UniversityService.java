package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.DuplicateResourceException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.entity.University;
import com.fime.ratemyprofs.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    /**
     * Obtiene la lista completa de universidades
     */
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    /**
     * Crea una nueva universidad
     */
    @Transactional
    public University createUniversity(University university) {
        // Verificar que no exista una universidad con el mismo nombre
        if (universityRepository.findByName(university.getName()).isPresent()) {
            throw new DuplicateResourceException("Ya existe una universidad con el nombre: " + university.getName());
        }
        
        return universityRepository.save(university);
    }

    /**
     * Actualiza una universidad existente
     */
    @Transactional
    public University updateUniversity(Integer id, University university) {
        University existing = universityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con ID: " + id));
        
        // Verificar que no exista otra universidad con el mismo nombre
        universityRepository.findByName(university.getName()).ifPresent(u -> {
            if (!u.getUniversityId().equals(id)) {
                throw new DuplicateResourceException("Ya existe otra universidad con el nombre: " + university.getName());
            }
        });
        
        existing.setName(university.getName());
        
        return universityRepository.save(existing);
    }

    /**
     * Elimina una universidad
     */
    @Transactional
    public void deleteUniversity(Integer id) {
        if (!universityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Universidad no encontrada con ID: " + id);
        }
        
        universityRepository.deleteById(id);
    }
}

