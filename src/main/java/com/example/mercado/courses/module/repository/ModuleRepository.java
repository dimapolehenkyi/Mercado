package com.example.mercado.courses.module.repository;

import com.example.mercado.courses.module.entity.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByIdAndCourseId(
            Long id,
            Long courseId
    );

    boolean existsByNameAndCourseId(
            String name,
            Long courseId
    );

    Page<Module> findAllByCourseIdAndDeletedFalseOrderByPositionAsc(
            Long courseId,
            Pageable pageable
    );

    long countByCourseIdAndDeletedFalse(
            Long courseId
    );

    @Modifying
    @Query("""
        UPDATE Module m
        SET m.deleted = true
        WHERE m.courseId = :courseId
    """)
    void softDeleteAllByCourseId(
            @Param("courseId") Long courseId
    );

    @Modifying
    @Query("""
        UPDATE Module module
        SET module.deleted = true
        WHERE module.id = :id
        AND module.courseId = :courseId
    """)
    void softDeleteByIdAndCourseId(
            @Param("id") Long id,
            @Param("courseId") Long courseId
    );

    @Query("""
        SELECT coalesce(max(module.position), -1)
        FROM Module module
        WHERE module.courseId = :courseId
    """)
    int findMaxPositionByCourseId(
            @Param("courseId") Long courseId
    );

    @Modifying
    @Query("""
        UPDATE Module module
        SET module.position = :position
        WHERE module.id = :id
    """)
    void updatePosition(
            @Param("id") Long id,
            @Param("position") Integer position
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Module module
        SET module.position = module.position + 1
        WHERE module.courseId = :courseId
        AND module.position >= :start
        AND module.position <= :end
    """)
    void incrementPositionRange(
            @Param("courseId") Long courseId,
            @Param("start") int start,
            @Param("end") int end
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Module module
        SET module.position = module.position - 1
        WHERE module.courseId = :courseId
        AND module.position >= :start
        AND module.position <= :end
    """)
    void decrementPositionRange(
            @Param("courseId") Long courseId,
            @Param("start") int start,
            @Param("end") int end
    );
}
