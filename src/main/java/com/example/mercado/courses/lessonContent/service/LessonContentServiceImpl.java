package com.example.mercado.courses.lessonContent.service;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;
import com.example.mercado.courses.lessonContent.entity.LessonContent;
import com.example.mercado.courses.lessonContent.exception.LessonContentAlreadyExists;
import com.example.mercado.courses.lessonContent.mapper.LessonContentMapper;
import com.example.mercado.courses.lessonContent.repository.LessonContentRepository;
import com.example.mercado.courses.lessonContent.service.interfaces.LessonContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasAuthority('ADMIN')")
public class LessonContentServiceImpl implements LessonContentService {


    private final LessonContentMapper mapper;
    private final LessonContentRepository repository;


    @Override
    public LessonContentResponse createLessonContent(Long lessonId, CreateLessonContentRequest request) {
        if (repository.existsByLessonIdAndName(lessonId, request.name())) {
            throw new LessonContentAlreadyExists(request.name(), lessonId);
        }

        LessonContent lessonContent = mapper.toEntity(lessonId, request);
        //нужно добавить установление позиций

        return mapper.toResponse(lessonContent);
    }

    @Override
    public LessonContentResponse updateLessonContent(Long lessonId, Long lessonContentId, UpdateLessonContentRequest request) {
        return null;
    }

    @Override
    public LessonContentResponse getLessonContentById(Long lessonId, Long lessonContentId) {
        return null;
    }

    @Override
    public void deleteLessonContent(Long lessonId, Long lessonContentId) {

    }

    @Override
    public List<LessonContentResponse> getAllLessonContents(Long lessonId) {
        return List.of();
    }
}
