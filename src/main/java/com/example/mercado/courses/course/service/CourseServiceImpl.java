package com.example.mercado.courses.course.service;

import com.example.mercado.courses.course.dto.*;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.exception.CourseAlreadyArchivedException;
import com.example.mercado.courses.course.exception.CourseAlreadyExistsException;
import com.example.mercado.courses.course.exception.CourseAlreadyPublishedException;
import com.example.mercado.courses.course.exception.CourseNotFound;
import com.example.mercado.courses.course.mapper.CourseMapper;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.interfaces.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse createCourse(CreateCourseRequest request) {
        if (courseRepository.existsByName(request.name())) {
            throw new CourseAlreadyExistsException(request.name());
        }

        Course course = courseMapper.toEntity(request);

        if (request.type() == CourseAccessType.FREE) {
            course.setPrice(BigDecimal.ZERO);
        }

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse updateCourse(Long courseId, UpdateCourseRequest request) {
         Course course = courseRepository.findById(courseId)
                 .orElseThrow(() -> new CourseNotFound(courseId));
        if (request.name() != null && !request.name().equals(course.getName())) {

            if (courseRepository.existsByName(request.name())) {
                throw new CourseAlreadyExistsException(request.name());
            }

            course.setName(request.name());
        }

        courseMapper.updateEntity(course, request);

        if (course.getType() == CourseAccessType.FREE) {
            course.setPrice(BigDecimal.ZERO);
        }

        courseRepository.save(course);

        return courseMapper.toResponse(course);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFound(courseId));
        if (course.getStatus() == CourseStatus.PUBLISHED) {
            throw new CourseAlreadyPublishedException(courseId);
        }
        course.setStatus(CourseStatus.PUBLISHED);
        courseRepository.save(course);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void archiveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFound(courseId));
        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new CourseAlreadyArchivedException(courseId);
        }
        course.setStatus(CourseStatus.ARCHIVED);
        courseRepository.save(course);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFound(courseId);
        }
        courseRepository.deleteById(courseId);
    }

    @Override
    @PreAuthorize("permitAll()")
    @Transactional(readOnly = true)
    public Page<CourseShortResponse> getAllCourses(Pageable pageable) {
        return courseRepository
                .findAll(pageable)
                .map(courseMapper::toShortResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseShortResponse> getCourseByTeacher(Long teacherId, Pageable pageable) {
        return courseRepository
                .findByTeacherId(teacherId, pageable)
                .map(courseMapper::toShortResponse);
    }

    @Override
    @PreAuthorize("permitAll()")
    @Transactional(readOnly = true)
    public Page<CourseShortResponse> searchCourse(
            CourseSearchFilter filter, Pageable pageable
    ) {
        return courseRepository.searchCourses(
                filter.keyword(),
                filter.type(),
                filter.teacherId(),
                filter.priceFrom(),
                filter.priceTo(),
                pageable
        ).map(courseMapper::toShortResponse);
    }

    @Override
    @PreAuthorize("permitAll()")
    @Transactional(readOnly = true)
    public Page<CourseShortResponse> getPublishedCourse(Pageable pageable) {
        return courseRepository
                .findByStatus(CourseStatus.PUBLISHED, pageable)
                .map(courseMapper::toShortResponse);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<CourseShortResponse> getArchivedCourse(Pageable pageable) {
        return courseRepository
                .findByStatus(CourseStatus.ARCHIVED, pageable)
                .map(courseMapper::toShortResponse);
    }

    @Override
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @Transactional(readOnly = true)
    public Page<CourseShortResponse> getMyCourse(Long userId, Pageable pageable) {
        return courseRepository
                .findAllByUserId(userId, pageable)
                .map(courseMapper::toShortResponse);
    }

    @Override
    @PreAuthorize("permitAll()")
    @Transactional(readOnly = true)
    public List<CourseShortResponse> getPopularCourses() {
        return courseRepository
                .findTop10ByStatusOrderByStudentCountDesc(CourseStatus.PUBLISHED)
                .stream()
                .map(courseMapper::toShortResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllCourses() {
        return courseRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new CourseNotFound(courseId));
        if (course.getStatus() == CourseStatus.ARCHIVED) {
            throw new CourseNotFound(courseId);
        }
        return courseMapper.toResponse(course);
    }
}
