package com.example.mercado.courses.lesson.repository;

import com.example.mercado.courses.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByModuleIdOrderByPositionAsc(Long moduleId);

    Optional<Lesson> findByIdAndModuleId(Long lessonId, Long moduleId);

    boolean existsByIdAndModuleId(Long lessonId, Long moduleId);

    long countByModuleId(Long moduleId);

    Optional<Lesson> findTopByModuleIdOrderByPositionDesc(Long moduleId);

}
