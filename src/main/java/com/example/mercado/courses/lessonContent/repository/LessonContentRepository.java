package com.example.mercado.courses.lessonContent.repository;

import com.example.mercado.courses.lessonContent.entity.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonContentRepository extends JpaRepository<LessonContent, Long> {

    List<LessonContent> findAllByLessonIdOrderByPositionAsc(Long lessonId);

    Optional<LessonContent> findByLessonIdAndId(Long lessonId, Long id);

    boolean existsByLessonIdAndName(Long lessonId, String name);

    boolean existsByLessonIdAndPosition(Long lessonId, Integer position);

    void deleteByLessonIdAndId(Long lessonId, Long id);

    Integer countByLessonId(Long lessonId);

}
