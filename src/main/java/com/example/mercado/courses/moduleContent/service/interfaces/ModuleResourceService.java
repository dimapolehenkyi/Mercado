package com.example.mercado.courses.moduleContent.service.interfaces;

import com.example.mercado.courses.moduleContent.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleContent.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleContent.dto.UpdateModuleResourceRequest;

import java.util.List;

public interface ModuleResourceService {

    ModuleResourceResponse createModuleResource(Long moduleId, CreateModuleResourceRequest request);
    ModuleResourceResponse updateModuleResource(Long moduleId, Long id, UpdateModuleResourceRequest request);
    ModuleResourceResponse getModuleResourceById(Long moduleId, Long moduleResourceId);

    void deleteModuleResource(Long moduleId, Long moduleResourceId);

    List<ModuleResourceResponse> getAllModuleResources(Long moduleId);
}
