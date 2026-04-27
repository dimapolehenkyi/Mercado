package com.example.mercado.courses.course.courseRequirement.mapper;

import com.example.mercado.configs.CentralMapperConfig;
import com.example.mercado.courses.course.courseRequirement.dto.AddRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.dto.RequirementResponse;
import com.example.mercado.courses.course.courseRequirement.dto.UpdateRequirementRequest;
import com.example.mercado.courses.course.courseRequirement.entity.CourseRequirement;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        config = CentralMapperConfig.class
)
public interface CourseRequirementMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "courseId", source = "courseId")
    CourseRequirement toEntity(Long courseId, AddRequirementRequest request);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "text", source = "text")
    void updateEntity(@MappingTarget CourseRequirement requirement, UpdateRequirementRequest request);

    RequirementResponse toResponse(CourseRequirement requirement);

}
