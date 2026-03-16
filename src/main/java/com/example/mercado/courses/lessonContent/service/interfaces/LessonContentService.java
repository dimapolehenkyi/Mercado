package com.example.mercado.courses.lessonContent.service.interfaces;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;

import java.util.List;

public interface LessonContentService {

    LessonContentResponse createLessonContent(Long lessonId, CreateLessonContentRequest request);
    LessonContentResponse updateLessonContent(Long lessonId, Long lessonContentId, UpdateLessonContentRequest request);
    LessonContentResponse getLessonContentById(Long lessonId, Long lessonContentId);

    void deleteLessonContent(Long lessonId, Long lessonContentId);

    List<LessonContentResponse> getAllLessonContents(Long lessonId);

}
