package com.example.mercado.courses.module.service.publicService;

import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModulePublicServiceImpl implements ModulePublicService {

    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    private final EntityFinder finder;

    @Override
    @Transactional(readOnly = true)
    public ModuleResponse getModuleById(
            Long courseId,
            Long moduleId
    ) {
        Module module = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(moduleId, courseId),
                ErrorCode.MODULE_NOT_FOUND,
                moduleId
        );
        return mapper.toResponse(module);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleShortResponse> getAllModulesByCourseId(
            Long courseId,
            Pageable pageable
    ) {
        return repository
                .findAllByCourseIdAndDeletedFalse(courseId, pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllModules(
            Long courseId
    ) {
        return repository.countByCourseIdAndDeletedFalse(courseId);
    }
}
