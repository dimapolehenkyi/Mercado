package com.example.mercado.courses.module.controller.adminController;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ReorderModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.service.adminService.ModuleAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/admin/courses/{courseId}/modules")
@RequiredArgsConstructor
public class ModuleAdminController {

    private final ModuleAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse createModule(
            @PathVariable @Positive Long courseId,
            @RequestBody @Valid CreateModuleRequest request
    ) {
        return service.createModule(courseId, request);
    }

    @PatchMapping("/{moduleId}")
    public ModuleResponse updateModule(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long moduleId,
            @RequestBody @Valid UpdateModuleRequest request
    ) {
        return service.updateModule(courseId, moduleId, request);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModule(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long moduleId
    ) {
        service.deleteModule(courseId, moduleId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllModules(
            @PathVariable @Positive Long courseId
    ) {
        service.deleteAllModulesByCourseId(courseId);
    }

    @PutMapping("/{moduleId}/position")
    public ModuleResponse updatePosition(
            @PathVariable @Positive Long courseId,
            @PathVariable @Positive Long moduleId,
            @RequestBody @Valid ReorderModuleRequest request
    ) {
        return service.updatePosition(courseId, moduleId, request);
    }

}
