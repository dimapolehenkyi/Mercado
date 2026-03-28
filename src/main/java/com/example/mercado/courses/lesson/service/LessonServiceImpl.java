package com.example.mercado.courses.lesson.service;

import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;
import com.example.mercado.courses.lesson.entity.Lesson;
import com.example.mercado.courses.lesson.exception.LessonAlreadyExistException;
import com.example.mercado.courses.lesson.exception.LessonNotFoundException;
import com.example.mercado.courses.lesson.mapper.LessonMapper;
import com.example.mercado.courses.lesson.repository.LessonRepository;
import com.example.mercado.courses.lesson.service.interfaces.LessonService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {


    private final LessonRepository repository;

    private final LessonMapper mapper;



    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public LessonResponse createLesson(Long moduleId, CreateLessonRequest request) {
        if (repository.existsByNameAndModuleId(request.name(), moduleId)) {
            throw new LessonAlreadyExistException(moduleId, request.name());
        }

        Lesson lesson = mapper.toEntity(request, moduleId);
        lesson.setPosition(repository.countByModuleId(moduleId) + 1);
        Lesson saved = repository.save(lesson);

        return mapper.toResponse(saved);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public LessonResponse updateLesson(Long moduleId, Long lessonId, UpdateLessonRequest request) {
        Lesson lesson = getLessonOrThrow(moduleId, lessonId);

        updateIfChanged(request.name(), lesson.getName(), lesson::setName);
        updateIfChanged(request.description(), lesson.getDescription(), lesson::setDescription);
        updateIfChanged(request.duration(), lesson.getDuration(), lesson::setDuration);
        updateIfChanged(request.status(), lesson.getStatus(), lesson::setStatus);

        Lesson saved = repository.save(lesson);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLesson(Long moduleId, Long lessonId) {
        Lesson lesson = getLessonOrThrow(moduleId, lessonId);

        return mapper.toResponse(lesson);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteLesson(Long moduleId, Long lessonId) {
        Lesson lesson = getLessonOrThrow(moduleId, lessonId);

        repository.delete(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByModuleId(Long moduleId) {
        List<Lesson> lessons = repository.findAllByModuleIdOrderByPositionAsc(moduleId);

        return lessons.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByModuleId(Long moduleId) {
        return repository.countByModuleId(moduleId);
    }




    private <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, currentValue)) {
            setter.accept(newValue);
        }
    }

    private @NonNull Lesson getLessonOrThrow(@NonNull Long moduleId, @NonNull Long lessonId) {
        return  repository.findByIdAndModuleId(lessonId, moduleId)
                .orElseThrow(() -> new LessonNotFoundException(lessonId, moduleId));
    }

}
