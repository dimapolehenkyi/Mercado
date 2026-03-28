package com.example.mercado.courses.assignment.repository;

import com.example.mercado.courses.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findAllByLessonIdOrderByPositionAsc(Long lessonId);

    Optional<Assignment> findByIdAndLessonId(Long assignmentId, Long lessonId);

    boolean existsByNameAndLessonId(String name, Long lessonId);

    Integer countByLessonId(Long lessonId);

}
