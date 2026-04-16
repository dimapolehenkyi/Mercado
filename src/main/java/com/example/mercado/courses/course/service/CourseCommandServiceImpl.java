package com.example.mercado.courses.course.service;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.interfaces.CourseCommandService;
import com.example.mercado.courses.course.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasAuthority('ADMIN')")
public class CourseCommandServiceImpl implements CourseCommandService {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    private final EntityFinder finder;


    @Override
    public CourseDetailsResponse createCourse(CreateCourseRequest request) {
        if (repository.existsByName(request.name())) {
            throw new AppException(
                    ErrorCode.COURSE_ALREADY_EXISTS,
                    request.name()
            );
        }

        Course course = mapper.toEntity(request);
        course.applyPricing(request.type(), request.price());

        Course savedCourse = repository.save(course);

        return mapper.toResponse(savedCourse);
    }

    @Override
    public CourseDetailsResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = finder.findEntityOrThrow(
                () -> repository.findById(courseId),
                ErrorCode.COURSE_NOT_FOUND,
                courseId
        );

        if (repository.existsByName(request.name())) {
            throw new AppException(
                    ErrorCode.COURSE_ALREADY_EXISTS,
                    request.name()
            );
        }

        mapper.updateEntity(course, request);
        course.setName(request.name());
        course.applyPricing(request.type(), request.price());

        Course saved = repository.save(course);

        return mapper.toResponse(saved);
    }

    @Override
    public void changeCourseStatus(Long courseId, ChangeStatusRequest request) {
        Course course = finder.findEntityOrThrow(
                () -> repository.findById(courseId),
                ErrorCode.COURSE_NOT_FOUND,
                courseId
        );

        course.setStatus(request.status());
        repository.save(course);
    }


    @Override
    public void changeCourseLevel(Long courseId, ChangeLevelRequest request) {
        Course course = finder.findEntityOrThrow(
                () -> repository.findById(courseId),
                ErrorCode.COURSE_NOT_FOUND,
                courseId
        );

        course.setLevel(request.level());
        repository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        Course course = finder.findEntityOrThrow(
                () -> repository.findById(courseId),
                ErrorCode.COURSE_NOT_FOUND,
                courseId
        );

        course.setStatus(CourseStatus.CLOSED);
    }

}
