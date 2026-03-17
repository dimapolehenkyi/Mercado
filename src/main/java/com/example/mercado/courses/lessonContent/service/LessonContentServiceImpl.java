package com.example.mercado.courses.lessonContent.service;

import com.example.mercado.courses.lessonContent.dto.CreateLessonContentRequest;
import com.example.mercado.courses.lessonContent.dto.LessonContentResponse;
import com.example.mercado.courses.lessonContent.dto.UpdateLessonContentRequest;
import com.example.mercado.courses.lessonContent.entity.LessonContent;
import com.example.mercado.courses.lessonContent.exception.LessonContentNotFoundException;
import com.example.mercado.courses.lessonContent.mapper.LessonContentMapper;
import com.example.mercado.courses.lessonContent.repository.LessonContentRepository;
import com.example.mercado.courses.lessonContent.service.interfaces.LessonContentService;
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
@PreAuthorize("hasAuthority('ADMIN')")
public class LessonContentServiceImpl implements LessonContentService {


    private final LessonContentMapper mapper;
    private final LessonContentRepository repository;


    @Override
    public LessonContentResponse createLessonContent(
            @NonNull Long lessonId,
            @NonNull CreateLessonContentRequest request
    ) {
        LessonContent lessonContent = mapper.toEntity(lessonId, request);

        lessonContent.setPosition(repository.countByLessonId(lessonId) + 1);

        return mapper.toResponse(repository.save(lessonContent));
    }

    @Override
    public LessonContentResponse updateLessonContent(
            @NonNull Long lessonId,
            @NonNull Long lessonContentId,
            @NonNull UpdateLessonContentRequest request
    ) {
        LessonContent lessonContent = getLessonContentOrThrow(lessonContentId, lessonId);

        updateIfChanged(request.name(), lessonContent.getName(), lessonContent::setName);
        updateIfChanged(request.description(), lessonContent.getDescription(), lessonContent::setDescription);
        updateIfChanged(request.type(), lessonContent.getType(), lessonContent::setType);
        updateIfChanged(request.url(), lessonContent.getUrl(), lessonContent::setUrl);
        updateIfChanged(request.thumbnailUrl(), lessonContent.getThumbnailUrl(), lessonContent::setThumbnailUrl);

        return mapper.toResponse(lessonContent);
    }

    @Override
    public LessonContentResponse getLessonContentById(
            @NonNull Long lessonId,
            @NonNull Long lessonContentId
    ) {
        LessonContent lessonContent = getLessonContentOrThrow(lessonContentId, lessonId);
        return mapper.toResponse(lessonContent);
    }

    @Override
    public void deleteLessonContent(
            @NonNull Long lessonId,
            @NonNull Long lessonContentId
    ) {
        LessonContent lessonContent = getLessonContentOrThrow(lessonContentId, lessonId);
        repository.delete(lessonContent);
    }

    @Override
    public List<LessonContentResponse> getAllLessonContents(
            @NonNull Long lessonId
    ) {
        List<LessonContent> lessonContents = repository.findAllByLessonIdOrderByPositionAsc(lessonId);
        return lessonContents.stream()
                .map(mapper::toResponse)
                .toList();
    }



    /*####################             HELPERS METHODS              ######################*/
    private LessonContent getLessonContentOrThrow(
            @NonNull Long lessonContentId,
            @NonNull Long lessonId
    ) {
        return  repository.findByLessonIdAndId(lessonId, lessonContentId)
                .orElseThrow(() -> new LessonContentNotFoundException(lessonContentId, lessonId));
    }

    private <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, currentValue)) {
            setter.accept(newValue);
        }
    }
}
