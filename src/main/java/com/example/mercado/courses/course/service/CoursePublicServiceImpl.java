package com.example.mercado.courses.course.service;

import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.enums.SortType;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.interfaces.CoursePublicService;
import com.example.mercado.courses.course.utils.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoursePublicServiceImpl implements CoursePublicService {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    private final EntityFinder finder;



    @Override
    public Page<CourseShortResponse> getCoursesByStatus(CourseStatus status, Pageable pageable) {
        return repository
                .findByStatus(status, pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    public Page<CourseShortResponse> getMyCourse(Long userId, Pageable pageable) {
        return repository
                .findAllByUserId(userId, pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    public Page<CourseShortResponse> getPopularCourses(Pageable pageable) {
        return repository
                .findAllByOrderByStudentCountDesc(pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    public CourseDetailsResponse getActiveCourseById(Long courseId) {
        Course course = finder.findEntityOrThrow(
                () -> repository.findActiveById(courseId),
                ErrorCode.COURSE_NOT_FOUND,
                courseId
        );

        return mapper.toResponse(course);
    }

    @Override
    public Page<CourseShortResponse> getAllCourses(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    public Page<CourseShortResponse> getCoursesByTeacherId(Long teacherId, Pageable pageable) {
        return repository
                .findAllByTeacherId(teacherId, pageable)
                .map(mapper::toShortResponse);
    }

    @Override
    public Page<CourseShortResponse> searchCourse(
            CourseSearchFilter filter,
            Pageable pageable
    ) {
        Pageable sortedPageable = applySorting(filter.sortType(), pageable);

        return repository.searchCourses(
                filter.keyword(),
                filter.type(),
                filter.teacherId(),
                filter.priceFrom(),
                filter.priceTo(),
                filter.level(),
                sortedPageable
        ).map(mapper::toShortResponse);
    }

    @Override
    public long countAllCourses() {
        return repository.count();
    }

    @Override
    public long countTeacherCoursesById(Long teacherId) {
        return repository.countByTeacherId(teacherId);
    }

    private Pageable applySorting(
            SortType sortType,
            Pageable pageable
    ) {
        if (sortType == null) {
            return pageable;
        }

        return switch (sortType) {
            case PRICE_ASC -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("price").ascending()
            );
            case PRICE_DESC -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("price").descending()
            );
            case NEWEST -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt").descending()
            );
            case RATING_ASC -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("rating").ascending()
            );
            case RATING_DESC -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("rating").descending()
            );
        };
    }

}
