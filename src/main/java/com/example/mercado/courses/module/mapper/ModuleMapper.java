package com.example.mercado.courses.module.mapper;

import com.example.mercado.configs.CentralMapperConfig;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        config = CentralMapperConfig.class
)
public interface ModuleMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "courseId", source = "courseId")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    Module toEntity(CreateModuleRequest request, Long courseId);

    ModuleResponse toResponse(Module module);

    ModuleShortResponse toShortResponse(Module module);

}
