package com.example.mercado.courses.lesson.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.lesson.dto.LessonShortResponse;
import com.example.mercado.courses.lesson.entity.Lesson;
import com.example.mercado.courses.lesson.enums.LessonStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("LessonRepository Test")
public class LessonRepositoryTest {

    @Autowired
    private LessonRepository repository;


    @Test
    @DisplayName("Func existsByNameAndModuleId should return true if exist")
    void existsByNameAndModuleId_shouldReturnTrue_ifExist() {
        Long moduleId = 1L;
        Lesson lesson = Lesson.builder()
                .moduleId(moduleId)
                .name("Test")
                .description("Test description")
                .duration(10)
                .position(1)
                .status(LessonStatus.PUBLISHED)
                .build();

        repository.save(lesson);

        boolean exist = repository.existsByNameAndModuleId(lesson.getName(), moduleId);

        Assertions.assertTrue(exist);
    }

    @Test
    @DisplayName("Func countByModuleId should return count")
    void countByModuleId_shouldReturnCount() {
        Long moduleId = 1L;

        Lesson lesson1 = Lesson.builder()
                .moduleId(moduleId)
                .name("Test")
                .description("Test description")
                .duration(10)
                .position(1)
                .status(LessonStatus.PUBLISHED)
                .build();

        Lesson lesson2 = Lesson.builder()
                .moduleId(moduleId)
                .name("Test 2")
                .description("Test 2 description")
                .duration(20)
                .position(2)
                .status(LessonStatus.PUBLISHED)
                .build();

        repository.saveAll(List.of(lesson1, lesson2));

        int count = repository.countByModuleId(moduleId);

        Assertions.assertEquals(2, count);
    }

    @Test
    @DisplayName("Func findByIdAndModuleId should return Lesson if exist")
    void findByIdAndModuleId_shouldReturnLesson_ifExist() {
        Long moduleId = 1L;
        Lesson lesson = Lesson.builder()
                .moduleId(moduleId)
                .name("Test")
                .description("Test description")
                .duration(10)
                .position(1)
                .status(LessonStatus.PUBLISHED)
                .build();

        repository.save(lesson);

        Optional<Lesson> foundLesson = repository.findByIdAndModuleId(lesson.getId(), moduleId);

        Assertions.assertTrue(foundLesson.isPresent());
    }

    @Test
    @DisplayName("Func findAllByModuleIdOrderByPositionAsc should return list")
    void findAllByModuleIdOrderByPositionAsc_shouldReturnList() {
        Long moduleId = 1L;

        List<Lesson> lessons = List.of(
                Lesson.builder()
                        .moduleId(moduleId)
                        .name("Test")
                        .description("Test description")
                        .duration(10)
                        .position(1)
                        .status(LessonStatus.PUBLISHED)
                        .build(),
                Lesson.builder()
                        .moduleId(moduleId)
                        .name("Test 2")
                        .description("Test 2 description")
                        .duration(20)
                        .position(2)
                        .status(LessonStatus.PUBLISHED)
                        .build()
        );

        repository.saveAll(lessons);

        List<Lesson> response = repository.findAllByModuleIdOrderByPositionAsc(moduleId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(1, response.get(0).getPosition());
        Assertions.assertEquals(2, response.get(1).getPosition());
        Assertions.assertTrue(response.stream().allMatch(l -> l.getModuleId().equals(moduleId)));
    }

}
