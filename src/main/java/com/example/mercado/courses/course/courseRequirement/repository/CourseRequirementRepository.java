package com.example.mercado.courses.course.courseRequirement.repository;

import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRequirementRepository extends JpaRepository<CourseRequirement, Long> {

    List<CourseRequirement> findAllByCourseIdOrderByPositionAsc(Long courseId);

    Optional<CourseRequirement> findByIdAndCourseId(Long id, Long courseId);

    boolean existsByCourseIdAndText(Long courseId, String text);

    void deleteByCourseId(Long courseId);

    @Modifying
    @Query("""
    update CourseRequirement r
    set r.position = :position
    where r.id = :id
""")
    void updatePosition(Long id, Integer position);

    int countByCourseId(Long courseId);

    void deleteAllByCourseId(Long courseId);

    @Query("""
    select coalesce(max(r.position), -1)
    from CourseRequirement r
    where r.courseId = :courseId
""")
    Integer findMaxPositionByCourseId(Long courseId);

    @Modifying
    @Query("""
    update CourseRequirement r
    set r.position = r.position + 1
    where r.courseId = :courseId
      and r.position >= :start
      and r.position <= :end
""")
    void incrementPositionRange(Long courseId, int start, int end);

    @Modifying
    @Query("""
    update CourseRequirement r
    set r.position = r.position - 1
    where r.courseId = :courseId
      and r.position >= :start
      and r.position <= :end
""")
    void decrementPositionRange(Long courseId, int start, int end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select r
    from CourseRequirement r
    where r.courseId = :courseId
""")
    void lockByCourseId(Long courseId);
}
