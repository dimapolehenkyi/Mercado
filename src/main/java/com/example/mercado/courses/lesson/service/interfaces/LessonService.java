package com.example.mercado.courses.lesson.service.interfaces;

import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;

import java.util.List;

public interface LessonService {

    LessonResponse createLesson(Long moduleId, CreateLessonRequest request);

    LessonResponse updateLesson(Long moduleId, Long lessonId, UpdateLessonRequest request);

    LessonResponse getLesson(Long moduleId, Long lessonId);

    void deleteLesson(Long moduleId, Long lessonId);

    List<LessonResponse> getLessonsByModuleId(Long moduleId);

    long countByModuleId(Long moduleId);

}
