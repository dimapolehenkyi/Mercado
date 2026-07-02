package com.example.mercado.courses.module.service.adminService;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;

public interface ModuleAdminService {

    ModuleResponse createModule(Long courseId, CreateModuleRequest request);
    ModuleResponse updateModule(Long courseId, Long moduleId, UpdateModuleRequest request);

    void deleteModule(Long courseId, Long moduleId);
    void deleteAllModulesByCourseId(Long courseId);

}
