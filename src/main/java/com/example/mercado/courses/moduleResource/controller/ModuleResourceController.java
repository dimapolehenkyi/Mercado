package com.example.mercado.courses.moduleResource.controller;

import com.example.mercado.courses.moduleResource.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleResource.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.service.interfaces.ModuleResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/modules/{moduleId}/resources")
public class ModuleResourceController {

    private final ModuleResourceService moduleResourceService;

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
