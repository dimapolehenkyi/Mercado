package com.example.mercado.courses.lesson.service;

import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.LessonShortResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;
import com.example.mercado.courses.lesson.entity.Lesson;
import com.example.mercado.courses.lesson.enums.LessonStatus;
import com.example.mercado.courses.lesson.mapper.LessonMapper;
import com.example.mercado.courses.lesson.repository.LessonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("LessonService Test")
public class LessonServiceTest {

    @Mock
    private LessonRepository repository;

    @Mock
    private LessonMapper mapper;

    @InjectMocks
    private LessonServiceImpl service;


    @Test
    @DisplayName("Func createLesson should return response")
    void createLesson_shouldReturnResponse() {
        Long moduleId = 1L;

        CreateLessonRequest request = new CreateLessonRequest(
                "Test",
                "Test description",
                10,
                LessonStatus.PUBLISHED
        );

        Lesson lesson = new Lesson();

        LessonResponse response = new LessonResponse(
                1L,
                moduleId,
                "Test",
                "Test description",
                LessonStatus.PUBLISHED,
                10,
                1
        );

        Mockito.when(repository.existsByNameAndModuleId(request.name(),  moduleId)).thenReturn(false);
        Mockito.when(repository.save(lesson)).thenReturn(lesson);
        Mockito.when(repository.countByModuleId(moduleId)).thenReturn(0);

        Mockito.when(mapper.toEntity(request, moduleId)).thenReturn(lesson);
        Mockito.when(mapper.toResponse(lesson)).thenReturn(response);

        LessonResponse created = service.createLesson(moduleId, request);

        Assertions.assertNotNull(created);
        Assertions.assertEquals(response,created);

        Mockito.verify(mapper, Mockito.times(1)).toResponse(lesson);
        Mockito.verify(mapper, Mockito.times(1)).toEntity(request, moduleId);
    }

    @Test
    @DisplayName("Func updateLesson should return response")
    void updateLesson_shouldReturnResponse() {
        Long moduleId = 1L;
        Long lessonId = 1L;

        Lesson lesson = new Lesson();

        UpdateLessonRequest request = new UpdateLessonRequest(
                "Updated test",
                "Updated description",
                20,
                LessonStatus.ARCHIVED
        );

        LessonResponse response = new LessonResponse(
                1L,
                moduleId,
                "Updated test",
                "Updated description",
                LessonStatus.ARCHIVED,
                20,
                1
        );

        Mockito.when(repository.save(lesson)).thenReturn(lesson);
        Mockito.when(repository.findByIdAndModuleId(lessonId, moduleId)).thenReturn(Optional.of(lesson));
        Mockito.when(mapper.toResponse(lesson)).thenReturn(response);

        LessonResponse updated = service.updateLesson(moduleId, lessonId, request);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(response,updated);

        Mockito.verify(mapper, Mockito.times(1)).toResponse(lesson);
        Mockito.verify(repository, Mockito.times(1)).findByIdAndModuleId(lessonId, moduleId);
        Mockito.verify(repository, Mockito.times(1)).save(lesson);
    }

    @Test
    @DisplayName("Func getLesson should return response")
    void getLesson_shouldReturnResponse() {
        Long moduleId = 1L;
        Long lessonId = 1L;

        Lesson lesson = new Lesson();

        LessonResponse response = new LessonResponse(
                1L,
                moduleId,
                "Test",
                "Test description",
                LessonStatus.PUBLISHED,
                10,
                1
        );

        Mockito.when(repository.findByIdAndModuleId(lessonId, moduleId)).thenReturn(Optional.of(lesson));
        Mockito.when(mapper.toResponse(lesson)).thenReturn(response);

        LessonResponse founded = service.getLesson(moduleId, lessonId);

        Assertions.assertNotNull(founded);
        Assertions.assertEquals(response,founded);
    }

    @Test
    @DisplayName("Func deleteLesson should delete lesson from database")
    void deleteLesson_shouldDeleteLesson_fromDatabase() {
        Long moduleId = 1L;
        Long lessonId = 1L;

        Lesson lesson = new Lesson();

        Mockito.when(repository.findByIdAndModuleId(lessonId, moduleId)).thenReturn(Optional.of(lesson));

        service.deleteLesson(moduleId, lessonId);

        Mockito.verify(repository, Mockito.times(1)).findByIdAndModuleId(lessonId, moduleId);
        Mockito.verify(repository, Mockito.times(1)).delete(lesson);
    }

    @Test
    @DisplayName("Func getLessonsByModuleId should return list of response")
    void getLessonsByModuleId_shouldReturnList_ofResponse() {
        long moduleId = 1L;

        List<Lesson> lessons = List.of(
                Lesson.builder()
                        .moduleId(moduleId)
                        .name("Test 1")
                        .description("Test description 1")
                        .position(1)
                        .duration(10)
                        .status(LessonStatus.PUBLISHED)
                        .build(),
                Lesson.builder()
                        .moduleId(moduleId)
                        .name("Test 2")
                        .description("Test description 2")
                        .position(2)
                        .duration(20)
                        .status(LessonStatus.ARCHIVED)
                        .build()
        );

        LessonResponse response1 = new LessonResponse(1L, moduleId, "Test 1", "Test description 1", LessonStatus.PUBLISHED, 10, 1);
        LessonResponse response2 = new LessonResponse(2L, moduleId, "Test 2", "Test description 2", LessonStatus.ARCHIVED, 20, 2);

        Mockito.when(repository.findAllByModuleIdOrderByPositionAsc(moduleId)).thenReturn(lessons);
        Mockito.when(mapper.toResponse(lessons.get(0))).thenReturn(response1);
        Mockito.when(mapper.toResponse(lessons.get(1))).thenReturn(response2);

        List<LessonResponse> response = service.getLessonsByModuleId(moduleId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(response1, response.get(0));
        Assertions.assertEquals(response2, response.get(1));

        Mockito.verify(repository).findAllByModuleIdOrderByPositionAsc(moduleId);
        Mockito.verify(mapper, Mockito.times(2)).toResponse(Mockito.any());
    }

    @Test
    @DisplayName("Func countByModuleId should return count")
    void countByModuleId_shouldReturnCount() {
        Long moduleId = 1L;

        Mockito.when(repository.countByModuleId(moduleId)).thenReturn(10);

        Long counted = service.countByModuleId(moduleId);

        Assertions.assertEquals(10, counted);
    }

}
