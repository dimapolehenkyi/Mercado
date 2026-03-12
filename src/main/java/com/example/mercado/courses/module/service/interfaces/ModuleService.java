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
    ModuleResponse updateModule(Long courseId, Long moduleId, UpdateModuleRequest request);
    ModuleResponse getModuleById(Long courseId, Long moduleId);

    void deleteModule(Long courseId, Long moduleId);

    ModuleResponse publishModule(Long courseId, Long moduleId);
    ModuleResponse archiveModule(Long courseId, Long moduleId);

    void moveModuleUp(Long courseId, Long moduleId);
    void moveModuleDown(Long courseId, Long moduleId);

    boolean existsById(Long moduleId);
    boolean existsByNameInCourse(Long courseId, String name);

    long countModulesByCourse(Long courseId);

    void deleteModulesByCourse(Long courseId);
    void updateStatusAllModules(Long courseId, ModuleStatus status);

    Page<ModuleShortResponse> getCourseModules(Long courseId, Pageable pageable);
    List<ModuleShortResponse> getAllCourseModules(Long courseId);
}
