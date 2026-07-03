package com.example.mercado.courses.module.service;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.module.service.interfaces.ModuleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.function.Consumer;

@Service
@Transactional
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModuleResponse createModule(@NonNull Long courseId, @NonNull CreateModuleRequest request) {
        if (moduleRepository.existsByNameAndCourseId(request.name(), courseId)) {
            throw new AppException(
                    ErrorCode.MODULE_ALREADY_EXISTS,
                    request.name()
            );
        }

        Module module = moduleMapper.toEntity(request, courseId);
        Module savedModule = moduleRepository.save(module);

        return moduleMapper.toResponse(savedModule);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModuleResponse updateModule(Long courseId , Long moduleId, UpdateModuleRequest request) {
        Module module = getModuleOrThrow(moduleId, courseId);

        updateIfChanged(request.name(), module.getName(), module::setName);
        updateIfChanged(request.description(), module.getDescription(), module::setDescription);

        return moduleMapper.toResponse(module);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModuleResponse getModuleById(Long courseId, Long moduleId) {
        Module module = getModuleOrThrow(moduleId, courseId);
        return moduleMapper.toResponse(module);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteModule(Long courseId, Long moduleId) {
        Module module = getModuleOrThrow(moduleId, courseId);
        if (module.isDeleted()) {
            throw new AppException(
                    ErrorCode.MODULE_ALREADY_DELETED,
                    moduleId
            );
        }
        module.setDeleted(true);
    }

    @Override
    @Transactional(readOnly = true)
    public long countModulesByCourse(Long courseId) {
        return moduleRepository.countByCourseIdAndDeletedFalse(courseId);
    }

    @Override
    public void deleteModulesByCourse(Long courseId) {
        moduleRepository.softDeleteAllByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleShortResponse> getCourseModules(Long courseId, Pageable pageable) {
        Page<Module> modules = moduleRepository
                .findAllByCourseIdAndDeletedFalseOrderByPositionAsc(courseId, pageable);
        return modules.map((Module t) -> moduleMapper.toShortResponse(t));
    }

    /*#######################            HELPERS METHODS              #######################*/

    private <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, currentValue)) {
            setter.accept(newValue);
        }
    }

    private Module getModuleOrThrow(Long moduleId, Long courseId) {
        return  moduleRepository.findByIdAndCourseId(moduleId, courseId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.MODULE_NOT_FOUND,
                        moduleId
                ));
    }
}
