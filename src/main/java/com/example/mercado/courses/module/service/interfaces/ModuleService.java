package com.example.mercado.courses.module.service.interfaces;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.enums.ModuleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModuleService {

    ModuleResponse createModule(Long courseId, CreateModuleRequest request);
    ModuleResponse updateModule(Long moduleId, UpdateModuleRequest request);
    ModuleResponse getModule(Long moduleId);

    void deleteModule(Long moduleId);

    ModuleResponse publishModule(Long moduleId);
    ModuleResponse archiveModule(Long moduleId);
    ModuleResponse changeStatus(Long moduleId, ModuleStatus moduleStatus);

    void moveModuleUp(Long moduleId);
    void moveModuleDown(Long moduleId);

    boolean existsById(Long moduleId);
    boolean existsByNameInCourse(Long moduleId, String names);

    long countModulesByCourse(Long courseId);

    void deleteModulesByCourse(Long courseId);
    void publishAllModules(Long courseId);

    Page<ModuleShortResponse> getCourseModules(Long courseId, Pageable pageable);
    List<ModuleShortResponse> getAllCourseModules(Long courseId);
}
