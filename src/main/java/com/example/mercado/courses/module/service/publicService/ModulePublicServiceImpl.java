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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ModulePublicServiceImpl implements ModulePublicService {

    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    private final EntityFinder finder;

    @Override
    public ModuleResponse getModuleById(
            Long moduleId,
            Long courseId
    ) {
        Module module = finder.findEntityOrThrow(
                () -> repository.findByIdAndCourseId(moduleId, courseId),
                ErrorCode.MODULE_NOT_FOUND,
                moduleId
        );
        return mapper.toResponse(module);
    }

    @Override
    public Page<ModuleShortResponse> getAllModulesByCourseId(
            Pageable pageable
    ) {
        return repository
                .findAll(pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    public long countAllModules(
            Long courseId
    ) {
        return repository.countByCourseIdAndDeletedFalse(courseId);
    }
}
