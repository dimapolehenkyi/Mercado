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

import java.util.Objects;

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
        if (repository.existsByNameAndCourseId(request.name(), courseId)) {
            throw new AppException(
                    ErrorCode.MODULE_ALREADY_EXISTS,
                    request.name()
            );
        }

        Integer maxPos = repository.findMaxPositionByCourseId(courseId);
        Integer nextPos = (maxPos == null) ? 0 : maxPos + 1;

        Module module = mapper.toEntity(request, courseId);
        module.setPosition(nextPos);

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

        Integer deletedPos = module.getPosition();
        Integer maxPos = repository.findMaxPositionByCourseId(courseId);

        repository.softDeleteByIdAndCourseId(moduleId, courseId);

        if (deletedPos < maxPos) {
            repository.decrementPositionRange(
                    courseId,
                    deletedPos + 1,
                    maxPos
            );
        }

        module.setPosition(maxPos + 1);
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

        Integer oldPos = current.getPosition();
        Integer newPos = request.position();
        Integer maxPos = repository.findMaxPositionByCourseId(courseId);

        if (newPos == null || newPos < 0 || newPos > maxPos) {
            throw new AppException(
                    ErrorCode.MODULE_POSITION_INVALID
            );
        }

        if (Objects.equals(oldPos, newPos)) {
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
