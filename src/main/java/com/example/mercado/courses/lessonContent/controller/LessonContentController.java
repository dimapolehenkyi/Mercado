package com.example.mercado.courses.lessonContent.controller;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;
import com.example.mercado.courses.lessonContent.service.interfaces.LessonContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/lessons/{lessonId}/contents")
public class LessonContentController {

    private final LessonContentService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public LessonContentResponse createLessonContent(
            @PathVariable Long lessonId,
            @RequestBody CreateLessonContentRequest request
    ) {
        return service.createLessonContent(lessonId, request);
    }

    @PatchMapping("/{contentId}")
    public LessonContentResponse updateLessonContent(
            @PathVariable Long lessonId,
            @PathVariable Long contentId,
            @RequestBody UpdateLessonContentRequest request
    ) {
        return service.updateLessonContent(lessonId, contentId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/{contentId}")
    public LessonContentResponse getLessonContentById(
            @PathVariable Long lessonId,
            @PathVariable Long contentId
    ) {
        return service.getLessonContentById(lessonId, contentId);
    }

    @DeleteMapping("/{contentId}")
    public void deleteLessonContent(
            @PathVariable Long lessonId,
            @PathVariable Long contentId
    ) {
        service.deleteLessonContent(lessonId, contentId);
    }

    @GetMapping
    public List<LessonContentResponse> getAllLessonContents(
            @PathVariable Long lessonId
    ) {
        return service.getAllLessonContents(lessonId);
    }

}
