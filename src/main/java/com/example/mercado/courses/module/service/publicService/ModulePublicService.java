package com.example.mercado.courses.module.service.publicService;

import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModulePublicService {

    ModuleResponse getModuleById(Long moduleId, Long courseId);

    Page<ModuleShortResponse> getAllModulesByCourseId(Pageable pageable);

    long countAllModules(Long courseId);

}
