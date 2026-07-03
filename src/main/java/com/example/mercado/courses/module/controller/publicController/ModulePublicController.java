package com.example.mercado.courses.module.controller.publicController;

import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.service.publicService.ModulePublicService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/courses/{courseId}/modules")
@RequiredArgsConstructor
public class ModulePublicController {

    private final ModulePublicService service;

    @GetMapping("/{moduleId}")
    public ModuleResponse getModuleById(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long moduleId
    ) {
        return service.getModuleById(courseId, moduleId);
    }

    @GetMapping
    public Page<ModuleShortResponse> getAllModules(
            @PathVariable @Positive Long courseId,
            @PageableDefault(
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return service.getAllModulesByCourseId(courseId, pageable);
    }

    @GetMapping("/count")
    public long countAllModules(
            @PathVariable @Positive Long courseId
    ) {
        return service.countAllModules(courseId);
    }

}
