package com.example.mercado.courses.module.repository;

import com.example.mercado.courses.module.enums.ModuleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    boolean existsByNameAndCourseId(Long moduleId, Long courseId);
    boolean existsByCourseId(Long courseId);

    Page<Module> findAllByCourseId(Long courseId, Pageable pageable);
    List<Module> findAllByCourseId(Long courseId);

    Page<Module> findAllByCourseIdAndStatus(Long courseId, ModuleStatus status, Pageable pageable);
    List<Module> findAllByStatus(ModuleStatus status);

    long countByCourseId(Long courseId);
    long countByCourseIdAndStatus(Long courseId, ModuleStatus status);

    void deleteAllByCourseId(Long courseId);
}
