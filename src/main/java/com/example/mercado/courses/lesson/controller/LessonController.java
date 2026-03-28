package com.example.mercado.courses.lesson.controller;

import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;
import com.example.mercado.courses.lesson.service.interfaces.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/modules/{moduleId}/lessons")
public class LessonController {

    private final LessonService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public LessonResponse createLesson(
            @PathVariable Long moduleId,
            @RequestBody CreateLessonRequest request
    ) {
        return service.createLesson(moduleId, request);
    }

    @PatchMapping("/{lessonId}")
    public LessonResponse updateLesson(
            @PathVariable Long moduleId,
            @PathVariable Long lessonId,
            @RequestBody UpdateLessonRequest request
    ) {
        return service.updateLesson(moduleId, lessonId, request);
    }

    @GetMapping("/{lessonId}")
    public LessonResponse getLesson(
            @PathVariable Long moduleId,
            @PathVariable Long lessonId
    ) {
        return service.getLesson(moduleId, lessonId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lessonId}")
    public void deleteLesson(
            @PathVariable Long moduleId,
            @PathVariable Long lessonId
    ) {
        service.deleteLesson(moduleId, lessonId);
    }

    @GetMapping
    public List<LessonResponse> getAllLessons(
            @PathVariable Long moduleId
    ) {
        return service.getLessonsByModuleId(moduleId);
    }

}
