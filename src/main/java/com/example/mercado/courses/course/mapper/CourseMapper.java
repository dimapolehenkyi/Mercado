package com.example.mercado.courses.course.mapper;

import com.example.mercado.configs.CentralMapperConfig;
import com.example.mercado.courses.course.dto.CourseDetailsResponse;
import com.example.mercado.courses.course.dto.CourseShortResponse;
import com.example.mercado.courses.course.dto.CreateCourseRequest;
import com.example.mercado.courses.course.dto.UpdateCourseRequest;
import com.example.mercado.courses.course.entity.Course;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        config = CentralMapperConfig.class
)
public interface CourseMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "shortDescription", source = "request.shortDescription")
    @Mapping(target = "previewVideoUrl", source = "request.previewVideoUrl")
    @Mapping(target = "thumbnailUrl", source = "request.thumbnailUrl")
    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "level", source = "request.level")
    Course toEntity(CreateCourseRequest request);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Course course, UpdateCourseRequest request);

    @Mapping(
            target = "studentCount",
            source = "studentCount",
            qualifiedByName = "defaultLong"
    )
    @Mapping(
            target = "rating",
            source = "rating",
            qualifiedByName = "defaultDouble"
    )
    @Mapping(
            target = "reviewsCount",
            source = "reviewsCount",
            qualifiedByName = "defaultLong"
    )
    @Mapping(
            target = "durationInMinutes",
            source = "durationInMinutes",
            qualifiedByName = "defaultInteger"
    )
    CourseDetailsResponse toResponse(Course course);

    @Mapping(
            target = "rating",
            source = "rating",
            qualifiedByName = "defaultDouble"
    )
    CourseShortResponse toShortResponse(Course course);



    @Named("defaultLong")
    default Long defaultLong(Long value) {
        return value != null ? value : 0L;
    }

    @Named("defaultDouble")
    default Double defaultDouble(Double value) {
        return value != null ? value : 0.0;
    }

    @Named("defaultInteger")
    default Integer defaultInteger(Integer value) {
        return value != null ? value : 0;
    }

}
