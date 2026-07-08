package com.example.mercado.integration.courses.module.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.module.service.publicService.ModulePublicService;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CoursePublicRequirementService Integration Test")
public class ModulePublicServiceImplTest extends AbstractRepositoryTest {

    @Autowired
    private ModuleRepository repository;

    @Autowired
    private ModulePublicService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    // =========================
    // getModuleById
    // =========================
    @Nested
    class GetModuleById {

        @Test
        void getModuleById_shouldReturnModuleResponse_whenModuleExistsAndCorrectRequest() {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setPosition(1);
                        m.setCourseId(courseId);
                    })
            );

            ModuleResponse result = service.getModuleById(courseId, module.getId());

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals("Module", result.name()),
                    () -> assertEquals(module.getId(), result.id()),
                    () -> assertEquals(1, result.position())
            );
        }

        @Test
        void getModuleById_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.getModuleById(courseId, moduleId)
            );

            assertAll(
                    () -> assertNotNull(ex),
                    () -> assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode())
            );
        }

    }

    // =========================
    // getAllModulesByCourseId
    // =========================
    @Nested
    class GetAllModulesByCourseId {

        @Test
        void getAllModulesByCourseId_shouldReturnPage_whenModulesExist() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            Pageable pageable = PageRequest.of(0, 5, Sort.by("position").ascending());
            Page<ModuleShortResponse> result = service.getAllModulesByCourseId(courseId, pageable);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(0, result.getContent().getFirst().position()),
                    () -> assertEquals(4, result.getContent().getLast().position())
            );
        }

        @Test
        void getAllModulesByCourseId_shouldReturnEmptyPage_whenModulesDoesNotExist() {
            Long courseId = 1L;

            Pageable pageable = PageRequest.of(0, 5);
            Page<ModuleShortResponse> result = service.getAllModulesByCourseId(courseId, pageable);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(0, result.getTotalElements()),
                    () -> assertEquals(0, result.getTotalPages())
            );
        }

    }

    // =========================
    // countAllModules
    // =========================
    @Nested
    class CountAllModules {

        @Test
        void countAllModules_shouldReturnCorrectNumberOfModulesInCourse_whenModulesExists() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            long result = service.countAllModules(courseId);

            assertAll(
                    () -> assertEquals(5, result)
            );
        }

        @Test
        void countAllModules_shouldReturnZero_whenModulesDoesNotExist() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            long result = service.countAllModules(2L);

            assertAll(
                    () -> assertEquals(0, result)
            );
        }

    }

}
