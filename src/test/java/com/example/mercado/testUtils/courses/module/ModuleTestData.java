package com.example.mercado.testUtils.courses.module;

import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ModuleTestData {

    /**
     * Creates ModuleShortResponse with default test values.
     * Fields can be overridden using lambda customizer.
     */
    public static ModuleShortResponse moduleShortResponse(
            Consumer<ModuleTestData.ShortArgs> customizer
    ) {
        ModuleTestData.ShortArgs m = new ModuleTestData.ShortArgs();
        customizer.accept(m);

        return new ModuleShortResponse(
                m.id,
                m.courseId,
                m.name,
                m.position
        );
    }

    /**
     * Creates ModuleResponse with default test values.
     * Fields can be overridden using lambda customizer.
     */
    public static ModuleResponse moduleResponse(
            Consumer<ModuleTestData.DetailsArgs> customizer
    ) {
        ModuleTestData.DetailsArgs a = new ModuleTestData.DetailsArgs();
        customizer.accept(a);

        return new ModuleResponse(
                a.id,
                a.courseId,
                a.name,
                a.description,
                a.deleted,
                a.position,
                a.createdAt,
                a.updatedAt
        );
    }

    public static ModuleResponse mapToResponse(
            Module module
    ) {
        return new ModuleResponse(
                module.getId(),
                module.getCourseId(),
                module.getName(),
                module.getDescription(),
                module.isDeleted(),
                module.getPosition(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }

    public static ModuleShortResponse mapToShortResponse(
            Module module
    ) {
        return new ModuleShortResponse(
                module.getId(),
                module.getCourseId(),
                module.getName(),
                module.getPosition()
        );
    }


    public static class ShortArgs {
        public Long id = 1L;
        public Long courseId = 1L;
        public String name = "Test module";
        public Integer position = 1;
    }

    public static class DetailsArgs {
        public Long id = 1L;
        public Long courseId = 1L;
        public String name = "Test module";
        public String description = "Test description";
        public Boolean deleted = false;
        public Integer position = 1;
        public LocalDateTime createdAt = LocalDateTime.now();
        public LocalDateTime updatedAt = LocalDateTime.now();
    }

}
