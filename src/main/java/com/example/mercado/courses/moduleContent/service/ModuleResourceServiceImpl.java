package com.example.mercado.courses.moduleContent.service;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.moduleContent.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleContent.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleContent.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleContent.entity.ModuleResource;
import com.example.mercado.courses.moduleContent.mapper.ModuleResourceMapper;
import com.example.mercado.courses.moduleContent.repository.ModuleResourceRepository;
import com.example.mercado.courses.moduleContent.service.interfaces.ModuleResourceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasAuthority('ADMIN')")
public class ModuleResourceServiceImpl implements ModuleResourceService {

    private final ModuleResourceMapper mapper;
    private final ModuleResourceRepository moduleResourceRepository;


    @Override
    public ModuleResourceResponse createModuleResource(Long moduleId, @NonNull CreateModuleResourceRequest request) {
        if (moduleResourceRepository.existsByModuleIdAndNameAndType(moduleId, request.name(), request.type())) {
            throw new AppException(
                    ErrorCode.MODULE_CONTENT_ALREADY_EXISTS,
                    request.name()
            );
        }

        ModuleResource moduleResource = mapper.toEntity(moduleId, request);
        moduleResource.setPosition(moduleResourceRepository.countByModuleId(moduleId) + 1);

        return mapper.toResponse(moduleResource);
    }

    @Override
    public ModuleResourceResponse updateModuleResource(Long moduleId, Long moduleResourceId, @NonNull UpdateModuleResourceRequest request) {
        ModuleResource moduleResource = findOrThrow(moduleId, moduleResourceId);

        updateIfChanged(request.name(), moduleResource.getName(), moduleResource::setName);
        updateIfChanged(request.type(), moduleResource.getType(), moduleResource::setType);
        updateIfChanged(request.url(), moduleResource.getUrl(), moduleResource::setUrl);
        updateIfChanged(request.thumbnailUrl(), moduleResource.getThumbnailUrl(), moduleResource::setThumbnailUrl);

        return mapper.toResponse(moduleResource);
    }

    @Override
    public ModuleResourceResponse getModuleResourceById(Long moduleId, Long moduleResourceId) {
        ModuleResource moduleResource = moduleResourceRepository.findByModuleIdAndId(moduleId, moduleResourceId)
                .orElseThrow(() -> new AppException(
                        ErrorCode.MODULE_CONTENT_NOT_FOUND,
                        moduleResourceId
                ));
        return mapper.toResponse(moduleResource);
    }

    @Override
    public void deleteModuleResource(Long moduleId, Long moduleResourceId) {
        ModuleResource moduleResource = findOrThrow(moduleId, moduleResourceId);
        moduleResourceRepository.delete(moduleResource);
    }

    @Override
    public List<ModuleResourceResponse> getAllModuleResources(Long moduleId) {
        List<ModuleResource> moduleResources = moduleResourceRepository.findAllByModuleIdOrderByPositionAsc(moduleId);
        return moduleResources.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /*#############################   HELPER METHODS   #############################*/

    private @NonNull ModuleResource findOrThrow(Long moduleId, Long id) {
        return moduleResourceRepository.findByModuleIdAndId(moduleId, id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.MODULE_CONTENT_NOT_FOUND,
                        id
                ));
    }

    private <T> void updateIfChanged(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !Objects.equals(newValue, currentValue)) {
            setter.accept(newValue);
        }
    }
}
