package com.example.mercado.courses.course.repository;

import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

    boolean existsByName(String name);
    boolean existsByIdAndStatus(Long id, CourseStatus status);

    long countByTeacherId(Long teacherId);

    Page<Course> findAllByUserId(Long userId, Pageable pageable);
    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
    Page<Course> findByTeacherId(Long teacherId, Pageable pageable);
    Page<Course> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    List<Course> findTop10ByStatusOrderByStudentCountDesc(CourseStatus status);

    @Query("""
            SELECT c FROM Course c
            WHERE (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:type IS NULL OR c.type = :type)
            AND (:teacherId IS NULL OR c.teacherId = :teacherId)
            AND (:priceFrom IS NULL OR c.price >= :priceFrom)
            AND (:priceTo IS NULL OR c.price <= :priceTo)
            AND c.status = 'PUBLISHED'
            """)
    Page<Course> searchCourses(
            String keyword,
            CourseAccessType type,
            Long teacherId,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            Pageable pageable
    );

}
