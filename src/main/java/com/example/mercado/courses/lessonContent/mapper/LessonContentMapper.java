package com.example.mercado.courses.lessonContent.mapper;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.entity.LessonContent;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LessonContentMapper {

    public LessonContent toEntity(@NonNull Long lessonId, @NonNull CreateLessonContentRequest request) {
        return LessonContent.builder()
                .lessonId(lessonId)
                .name(request.name())
                .description(request.description())
                .url(request.url())
                .thumbnailUrl(request.thumbnailUrl())
                .type(request.type())
                .build();
    }

    public LessonContentResponse toResponse(@NonNull LessonContent entity) {
        return new LessonContentResponse(
                entity.getId(),
                entity.getLessonId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPosition(),
                entity.getUrl(),
                entity.getThumbnailUrl(),
                entity.getType()
        );
    }

}
