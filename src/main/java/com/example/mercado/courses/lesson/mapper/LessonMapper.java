package com.example.mercado.courses.lesson.mapper;

import com.example.mercado.courses.lesson.dto.CreateLessonRequest;
import com.example.mercado.courses.lesson.dto.LessonResponse;
import com.example.mercado.courses.lesson.dto.LessonShortResponse;
import com.example.mercado.courses.lesson.dto.UpdateLessonRequest;
import com.example.mercado.courses.lesson.entity.Lesson;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public Lesson toEntity(@NonNull CreateLessonRequest request, @NonNull Long moduleId) {
        return Lesson.builder()
                .moduleId(moduleId)
                .name(request.name())
                .description(request.description())
                .status(request.status())
                .build();
    }

    public void updateEntity(@NonNull Lesson lesson, @NonNull UpdateLessonRequest request) {
        lesson.setName(request.name());
        lesson.setDescription(request.description());
        lesson.setDuration(request.duration());
        lesson.setStatus(request.status());
    }

    public LessonResponse toResponse(@NonNull Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getModuleId(),
                lesson.getName(),
                lesson.getDescription(),
                lesson.getStatus(),
                lesson.getDuration(),
                lesson.getPosition()
        );
    }

    public LessonShortResponse toShortResponse(@NonNull Lesson lesson) {
        return new LessonShortResponse(
                lesson.getId(),
                lesson.getModuleId(),
                lesson.getName(),
                lesson.getStatus()
        );
    }

}
