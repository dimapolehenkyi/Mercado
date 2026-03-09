package com.example.mercado.courses.module.service;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.service.interfaces.ModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ModuleServiceImpl implements ModuleService {
    @Override
    public ModuleResponse createModule(Long courseId, CreateModuleRequest request) {
        return null;
    }

    @Override
    public ModuleResponse updateModule(Long moduleId, UpdateModuleRequest request) {
        return null;
    }

    @Override
    public ModuleResponse getModule(Long moduleId) {
        return null;
    }

    @Override
    public void deleteModule(Long moduleId) {

    }

    @Override
    public ModuleResponse publishModule(Long moduleId) {
        return null;
    }

    @Override
    public ModuleResponse archiveModule(Long moduleId) {
        return null;
    }

    @Override
    public ModuleResponse changeStatus(Long moduleId, ModuleStatus moduleStatus) {
        return null;
    }

    @Override
    public void moveModuleUp(Long moduleId) {

    }

    @Override
    public void moveModuleDown(Long moduleId) {

    }

    @Override
    public boolean existsById(Long moduleId) {
        return false;
    }

    @Override
    public boolean existsByNameInCourse(Long moduleId, String names) {
        return false;
    }

    @Override
    public long countModulesByCourse(Long courseId) {
        return 0;
    }

    @Override
    public void deleteModulesByCourse(Long courseId) {

    }

    @Override
    public void publishAllModules(Long courseId) {

    }

    @Override
    public Page<ModuleShortResponse> getCourseModules(Long courseId, Pageable pageable) {
        return null;
    }

    @Override
    public List<ModuleShortResponse> getAllCourseModules(Long courseId) {
        return List.of();
    }
}
