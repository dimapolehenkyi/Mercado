package com.example.mercado.courses.course.courseLearningPoint.mapper;

import com.example.mercado.configs.CentralMapperConfig;
import com.example.mercado.courses.course.courseLearningPoint.dto.AddLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.dto.LearningPointResponse;
import com.example.mercado.courses.course.courseLearningPoint.dto.UpdateLearningPointRequest;
import com.example.mercado.courses.course.courseLearningPoint.entity.CourseLearningPoint;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        config = CentralMapperConfig.class
)
public interface CourseLearningPointMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "courseId", source = "courseId")
    CourseLearningPoint toEntity(Long courseId, AddLearningPointRequest request);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "text", source = "text")
    void updateEntity(@MappingTarget CourseLearningPoint point, UpdateLearningPointRequest request);

    LearningPointResponse toResponse(CourseLearningPoint point);

}
