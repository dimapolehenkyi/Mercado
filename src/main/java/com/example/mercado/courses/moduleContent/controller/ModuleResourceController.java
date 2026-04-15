package com.example.mercado.courses.moduleContent.controller;

import com.example.mercado.courses.moduleContent.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleContent.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleContent.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleContent.service.interfaces.ModuleResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/modules/{moduleId}/resources")
public class ModuleResourceController {

    private final ModuleResourceService moduleResourceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ModuleResourceResponse createModuleResource(
            @PathVariable Long moduleId,
            @RequestBody CreateModuleResourceRequest request
    ) {
        return moduleResourceService.createModuleResource(moduleId, request);
    }

    @PatchMapping("/{resourceId}")
    public ModuleResourceResponse updateModuleResource(
            @PathVariable Long moduleId,
            @PathVariable Long resourceId,
            @RequestBody UpdateModuleResourceRequest request
    ) {
        return moduleResourceService.updateModuleResource(moduleId, resourceId, request);
    }

    @GetMapping("/{resourceId}")
    public ModuleResourceResponse getModuleResourceById(
            @PathVariable Long moduleId,
            @PathVariable Long resourceId
    ) {
        return moduleResourceService.getModuleResourceById(moduleId, resourceId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{resourceId}")
    public void deleteModuleResource(
            @PathVariable Long moduleId,
            @PathVariable Long resourceId
    ) {
        moduleResourceService.deleteModuleResource(moduleId, resourceId);
    }

    @GetMapping
    public List<ModuleResourceResponse> getAllModuleResources(
            @PathVariable Long moduleId
    ) {
        return moduleResourceService.getAllModuleResources(moduleId);
    }
}
