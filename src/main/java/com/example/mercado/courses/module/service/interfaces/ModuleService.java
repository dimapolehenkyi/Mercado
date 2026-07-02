package com.example.mercado.courses.module.service.interfaces;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModuleService {

    ModuleResponse createModule(Long courseId, CreateModuleRequest request);
    ModuleResponse updateModule(Long courseId, Long moduleId, UpdateModuleRequest request);
    ModuleResponse getModuleById(Long courseId, Long moduleId);

    void deleteModule(Long courseId, Long moduleId);

    //delete
    long countModulesByCourse(Long courseId);

    //delete
    void deleteModulesByCourse(Long courseId);

    Page<ModuleShortResponse> getCourseModules(Long courseId, Pageable pageable);
}
