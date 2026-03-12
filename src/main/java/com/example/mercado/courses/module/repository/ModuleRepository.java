package com.example.mercado.courses.module.repository;


import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.enums.ModuleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    boolean existsByNameAndCourseId(String name, Long courseId);
    boolean existsByCourseId(Long courseId);
    boolean existsByName(String name);

    Page<Module> findAllByCourseIdAndDeletedFalseOrderByPositionAsc(Long courseId, Pageable pageable);
    List<Module> findAllByCourseIdAndDeletedFalseOrderByPositionAsc(Long courseId);

    Page<Module> findAllByCourseIdAndStatus(Long courseId, ModuleStatus status, Pageable pageable);
    List<Module> findAllByStatus(ModuleStatus status);

    long countByCourseIdAndDeletedFalse(Long courseId);
    long countByCourseIdAndStatus(Long courseId, ModuleStatus status);

    @Modifying
    @Query(
            """
        UPDATE Module m
        SET m.deleted = true
        WHERE m.courseId = :courseId
        """
    )
    void softDeleteAllByCourseId(Long courseId);

    @Modifying
    @Query(
            """
        UPDATE Module m
        SET m.status = :status
        WHERE m.courseId = :courseId
       """
    )
    void updateStatusByCourseId(Long courseId, ModuleStatus status);

    Optional<Module> findByCourseIdAndPosition(Long courseId, int i);
    Optional<Module> findByIdAndCourseId(Long id, Long courseId);
}
