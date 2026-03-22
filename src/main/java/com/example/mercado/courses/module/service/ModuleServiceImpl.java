package com.example.mercado.courses.module.service;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.exception.*;
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

import java.util.List;
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
            throw new ModuleAlreadyExistsException(request.name());
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
        updateIfChanged(request.moduleType(), module.getModuleType(), module::setModuleType);
        updateIfChanged(request.status(), module.getStatus(), module::setStatus);

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
            throw new ModuleAlreadyDeletedException(moduleId);
        }
        module.setDeleted(true);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModuleResponse publishModule(Long courseId, Long moduleId) {
        Module module = getModuleOrThrow(moduleId, courseId);
        ensureStatusNot(module, ModuleStatus.PUBLISHED, new ModuleAlreadyPublishedException(moduleId));
        module.setStatus(ModuleStatus.PUBLISHED);
        return moduleMapper.toResponse(module);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModuleResponse archiveModule(Long courseId, Long moduleId) {
        Module module = getModuleOrThrow(moduleId, courseId);
        ensureStatusNot(module, ModuleStatus.ARCHIVED, new ModuleAlreadyArchivedException(moduleId));
        module.setStatus(ModuleStatus.ARCHIVED);
        return moduleMapper.toResponse(module);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void moveModuleUp(Long courseId, Long moduleId) {
        Module module = getModuleOrThrow(moduleId, courseId);

        if (module.getPosition() == 0) {
            throw new ModuleAlreadyHaveFirstPosition(module.getId());
        }

        int currentPosition = module.getPosition();

        Module previous = moduleRepository.findByCourseIdAndPosition(module.getCourseId(), module.getPosition() - 1)
                .orElseThrow(() -> new ModuleNotFoundException(module.getCourseId(), module.getPosition() - 1));

        previous.setPosition(currentPosition);
        module.setPosition(currentPosition - 1);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void moveModuleDown(Long courseId, Long moduleId) {
        Module module = getModuleOrThrow(moduleId, courseId);

        int currentPosition = module.getPosition();

        Module next = moduleRepository.findByCourseIdAndPosition(module.getCourseId(), module.getPosition() + 1)
                .orElseThrow(() -> new ModuleAlreadyHaveLastPositionException(moduleId));

        next.setPosition(currentPosition);
        module.setPosition(currentPosition + 1);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long moduleId) {
        return moduleRepository.existsById(moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameInCourse(Long courseId, String name) {
        return moduleRepository.existsByNameAndCourseId(name, courseId);
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
    public void updateStatusAllModules(Long courseId, ModuleStatus status) {
        moduleRepository.updateStatusByCourseId(courseId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleShortResponse> getCourseModules(Long courseId, Pageable pageable) {
        Page<Module> modules = moduleRepository
                .findAllByCourseIdAndDeletedFalseOrderByPositionAsc(courseId, pageable);
        return modules.map(moduleMapper::toShortResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleShortResponse> getAllCourseModules(Long courseId) {
        List<Module> modules = moduleRepository
                .findAllByCourseIdAndDeletedFalseOrderByPositionAsc(courseId);
        return modules.stream()
                .map(moduleMapper::toShortResponse)
                .toList();
    }





    /*#######################            HELPERS METHODS              #######################*/

    private <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, currentValue)) {
            setter.accept(newValue);
        }
    }

    private Module getModuleOrThrow(Long moduleId, Long courseId) {
        return  moduleRepository.findByIdAndCourseId(moduleId, courseId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));
    }

    private void ensureStatusNot(Module module, ModuleStatus status, RuntimeException ex) {
        if (module.getStatus() == status) {
            throw ex;
        }
    }
}
