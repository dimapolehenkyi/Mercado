package com.example.mercado.courses.module.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ReorderModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class ModuleAdminServiceImpl implements ModuleAdminService {

    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    private final EntityFinder finder;

    @Override
    @Transactional
    public ModuleResponse createModule(
            Long courseId,
            CreateModuleRequest request
    ) {
        if (repository.existsByNameAndCourseId(request.name(),  courseId)) {
            throw new AppException(
                    ErrorCode.MODULE_ALREADY_EXISTS,
                    request.name()
            );
        }

        Module module = mapper.toEntity(request, courseId);

        return mapper.toResponse(module);
    }

    @Override
    @Transactional
    public ModuleResponse updateModule(
            Long courseId,
            Long moduleId,
            UpdateModuleRequest request
    ) {
        Module module = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(moduleId, courseId),
                ErrorCode.MODULE_NOT_FOUND,
                moduleId
        );

        if (request.name() != null
                && !request.name().equals(module.getName())
                && repository.existsByNameAndCourseId(request.name(), courseId)) {
            throw new AppException(
                    ErrorCode.MODULE_ALREADY_EXISTS,
                    request.name()
            );
        }

        module.update(request);

        return mapper.toResponse(module);
    }

    @Override
    @Transactional
    public void deleteModule(
            Long courseId,
            Long moduleId
    ) {
        Module module = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(moduleId, courseId),
                ErrorCode.MODULE_NOT_FOUND,
                moduleId
        );

        repository.softDeleteByIdAndCourseId(moduleId, courseId);
    }

    @Override
    @Transactional
    public void deleteAllModulesByCourseId(
            Long courseId
    ) {
        repository.softDeleteAllByCourseId(courseId);
    }

    @Override
    @Transactional
    public ModuleResponse updatePosition(
            Long courseId,
            Long moduleId,
            ReorderModuleRequest request
    ) {
        Module current = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(moduleId, courseId),
                ErrorCode.MODULE_NOT_FOUND,
                moduleId
        );

        int oldPos = current.getPosition();
        int newPos = request.position();

        int maxPos = repository.findMaxPositionByCourseId(courseId);

        if (newPos < 0 || newPos > maxPos) {
            throw new AppException(
                    ErrorCode.MODULE_POSITION_INVALID
            );
        }

        if (oldPos == newPos) {
            return mapper.toResponse(current);
        }

        if (newPos < oldPos) {
            repository.incrementPositionRange(
                    courseId,
                    newPos,
                    oldPos - 1
            );
        } else {
            repository.decrementPositionRange(
                    courseId,
                    oldPos + 1,
                    newPos
            );
        }

        repository.updatePosition(
                moduleId,
                newPos
        );

        return mapper.toResponse(current);
    }
}
