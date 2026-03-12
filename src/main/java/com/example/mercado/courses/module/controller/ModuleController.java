package com.example.mercado.courses.module.controller;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.service.interfaces.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse createModule(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateModuleRequest request
    ) {
        return moduleService.createModule(courseId, request);
    }

    @PatchMapping("/{moduleId}")
    public ModuleResponse updateModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId,
            @RequestBody UpdateModuleRequest request
    ) {
        return moduleService.updateModule(courseId, moduleId, request);
    }

    @GetMapping("/{moduleId}")
    public ModuleResponse getModuleById(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        return moduleService.getModuleById(courseId, moduleId);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        moduleService.deleteModule(courseId, moduleId);
    }

    @PatchMapping("/{moduleId}/publish")
    public ModuleResponse publishModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        return moduleService.publishModule(courseId, moduleId);
    }

    @PatchMapping("/{moduleId}/archive")
    public ModuleResponse archiveModule(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        return moduleService.archiveModule(courseId, moduleId);
    }

    @PatchMapping("/{moduleId}/move-up")
    public void moveModuleUp (
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        moduleService.moveModuleUp(courseId, moduleId);
    }

    @PatchMapping("/{moduleId}/move-down")
    public void moveModuleDown(
            @PathVariable Long courseId,
            @PathVariable Long moduleId
    ) {
        moduleService.moveModuleDown(courseId, moduleId);
    }

    @GetMapping
    public Page<ModuleShortResponse> getCourseModules(
            @PathVariable Long courseId,
            Pageable pageable
    ) {
        return moduleService.getCourseModules(courseId, pageable);
    }

}
