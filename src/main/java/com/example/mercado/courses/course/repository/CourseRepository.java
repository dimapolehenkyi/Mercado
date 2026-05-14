package com.example.mercado.courses.course.repository;

import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
            SELECT c FROM Course c
            WHERE c.name = :name
            AND c.status = 'PUBLISHED'
            AND c.deleted = false
            """)
    Optional<Course> findByName(
            @Param("name") String name
    );

    @Query("""
            SELECT c FROM Course c
            WHERE c.id = :id
            AND c.deleted = false
            AND c.status = 'PUBLISHED'
            """)
    Optional<Course> findActiveById(Long id);

    boolean existsByName(String name);
    boolean existsByIdAndStatus(Long id, CourseStatus status);

    long countByTeacherId(Long teacherId);

    @Query("""
            SELECT c FROM Course c
            WHERE c.status = 'PUBLISHED'
            AND c.deleted = false
            AND c.userId = :userId
            ORDER BY c.id DESC
            """)
    Page<Course> findAllByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Course c
            WHERE c.status = :status
            AND c.deleted = false
            ORDER BY c.id DESC
            """)
    Page<Course> findAllByStatus(
            @Param("status") CourseStatus status,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Course c
            WHERE c.status = 'PUBLISHED'
            AND c.deleted = false
            AND c.teacherId = :teacherId
            ORDER BY c.id DESC
            """)
    Page<Course> findAllByTeacherId(
            @Param("teacherId") Long teacherId,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Course c
            WHERE c.status = 'PUBLISHED'
            AND c.deleted = false
            ORDER BY c.studentCount DESC
            """)
    Page<Course> findPopularPublishedCourses(Pageable pageable);

    @Query("""
            SELECT c FROM Course c
            WHERE (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:type IS NULL OR c.type = :type)
            AND (:teacherId IS NULL OR c.teacherId = :teacherId)
            AND (:priceFrom IS NULL OR c.price >= :priceFrom)
            AND (:priceTo IS NULL OR c.price <= :priceTo)
            AND (:level IS NULL OR c.level = :level)
            AND c.status = 'PUBLISHED'
            """)
    Page<Course> searchCourses(
            String keyword,
            CourseAccessType type,
            Long teacherId,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            CourseLevel level,
            Pageable pageable
    );

}
