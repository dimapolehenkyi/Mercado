package com.example.mercado.courses.lessonContent.repository;

import com.example.mercado.courses.lessonContent.entity.LessonContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("LessonContentRepository Tests")
class LessonContentRepositoryTest {

    @Autowired
    private LessonContentRepository repository;

    @Test
    @DisplayName("Func findAllByLessonIdOrderByPositionAsc should return ordered contents")
    void testFindAllByLessonIdOrderByPositionAsc() {
        LessonContent content1 = LessonContent.builder()
                .lessonId(1L)
                .name("Content 1")
                .position(2)
                .build();

        LessonContent content2 = LessonContent.builder()
                .lessonId(1L)
                .name("Content 2")
                .position(1)
                .build();

        repository.save(content1);
        repository.save(content2);

        List<LessonContent> contents = repository.findAllByLessonIdOrderByPositionAsc(1L);

        Assertions.assertEquals(2, contents.size());
        Assertions.assertEquals(1, contents.get(0).getPosition());
        Assertions.assertEquals(2, contents.get(1).getPosition());
    }

    @Test
    @DisplayName("Func findByLessonIdAndId should return optional content")
    void testFindByLessonIdAndId() {
        LessonContent content = LessonContent.builder()
                .lessonId(1L)
                .name("Content")
                .position(1)
                .build();

        LessonContent saved = repository.save(content);

        Optional<LessonContent> found = repository.findByLessonIdAndId(1L, saved.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Content", found.get().getName());
    }

    @Test
    @DisplayName("Func existsByLessonIdAndName should return true if exists")
    void testExistsByLessonIdAndName() {
        repository.save(LessonContent.builder()
                .lessonId(1L)
                .name("MyContent")
                .position(1)
                .build());

        boolean exists = repository.existsByLessonIdAndName(1L, "MyContent");
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Func existsByLessonIdAndPosition should return true if position exists")
    void testExistsByLessonIdAndPosition() {
        repository.save(LessonContent.builder()
                .lessonId(1L)
                .name("Content")
                .position(5)
                .build());

        boolean exists = repository.existsByLessonIdAndPosition(1L, 5);
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Func deleteByLessonIdAndId should remove content")
    void testDeleteByLessonIdAndId() {
        LessonContent content = repository.save(LessonContent.builder()
                .lessonId(1L)
                .name("ToDelete")
                .position(1)
                .build());

        repository.deleteByLessonIdAndId(1L, content.getId());

        boolean exists = repository.existsByLessonIdAndName(1L, "ToDelete");
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("Func countByLessonId should return correct count")
    void testCountByLessonId() {
        repository.save(LessonContent.builder().lessonId(2L).name("C1").position(1).build());
        repository.save(LessonContent.builder().lessonId(2L).name("C2").position(2).build());

        Integer count = repository.countByLessonId(2L);
        Assertions.assertEquals(2, count);
    }
}
