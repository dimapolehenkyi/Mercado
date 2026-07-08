package com.example.mercado.integration.courses.module.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ReorderModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.module.service.adminService.ModuleAdminService;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Module-Admin Service Integration Test")
public class ModuleAdminServiceImplTest extends AbstractRepositoryTest {

    @Autowired
    private ModuleRepository repository;

    @Autowired
    private ModuleAdminService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    // =========================
    // createModule
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class CreateModule {

        @Test
        void createModule_shouldCreateModuleWithZeroPosition_whenCourseHasNotModules() {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module";
                m.description = "Description";
            });

            ModuleResponse response = service.createModule(courseId, request);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertFalse(result.isEmpty()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(1, result.getTotalElements()),

                    () -> assertEquals(response.name(), result.getContent().getFirst().getName()),
                    () -> assertEquals(response.description(), result.getContent().getFirst().getDescription()),
                    () -> assertEquals(response.id(), result.getContent().getFirst().getId()),
                    () -> assertEquals(response.position(), result.getContent().getFirst().getPosition())
            );
        }

        @Test
        void createModule_shouldCreateModuleWithNextPosition_whenCourseAlreadyHasModules() {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module Test";
                m.description = "Description";
            });

            for (int i = 1; i <= 2; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            ModuleResponse response = service.createModule(courseId, request);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertFalse(result.isEmpty()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(3, result.getTotalElements()),

                    () -> assertEquals("Module 1", result.getContent().getFirst().getName()),
                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),

                    () -> assertEquals("Module 2", result.getContent().get(1).getName()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),

                    () -> assertEquals(response.name(), result.getContent().get(2).getName()),
                    () -> assertEquals(response.position(), result.getContent().get(2).getPosition())
            );
        }

        @Test
        void createModule_shouldThrowException_whenModuleWithSameNameAlreadyExistsInCourse() {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module Test");
                        m.setDescription("Description");
                        m.setCourseId(courseId);
                    })
            );

            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module Test";
                m.description = "Description";
            });

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.createModule(courseId, request)
            );

            assertAll(
                    () -> assertEquals(ErrorCode.MODULE_ALREADY_EXISTS, ex.getCode())
            );
        }

    }

    // =========================
    // updateModule
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class UpdateModule {

        @Test
        void updateModule_shouldCorrectlyUpdateModule_whenValidRequest() {
            Long courseId = 1L;
            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module Test");
                m.setDescription("Description");
                m.setCourseId(courseId);
                m.setPosition(1);
            }));
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "Updated name";
                m.description = "Updated description";
            });

            ModuleResponse response = service.updateModule(courseId, module.getId(), request);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(response.name(), result.getContent().getFirst().getName()),
                    () -> assertEquals(response.description(), result.getContent().getFirst().getDescription()),
                    () -> assertEquals(response.position(), result.getContent().getFirst().getPosition()),
                    () -> assertEquals(response.id(), result.getContent().getFirst().getId()),
                    () -> assertEquals(response.courseId(), result.getContent().getFirst().getCourseId())
            );
        }

        @Test
        void updateModule_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;

            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "Updated name";
                m.description = "Updated description";
            });

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.updateModule(courseId, moduleId, request)
            );

            assertAll(
                    () -> assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode())
            );
        }

        @Test
        void updateModule_shouldThrowException_whenSameNameAlreadyExistsInCourse() {
            Long courseId = 1L;

            Module module_one = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module Test");
                m.setDescription("Description");
                m.setCourseId(courseId);
                m.setPosition(1);
            }));
            Module module_two = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module two");
                m.setDescription("Description");
                m.setCourseId(courseId);
                m.setPosition(2);
            }));
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "Module two";
                m.description = "Updated description";
            });

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.updateModule(courseId, module_one.getId(), request)
            );

            assertAll(
                    () -> assertEquals(ErrorCode.MODULE_ALREADY_EXISTS, ex.getCode())
            );
        }

    }

    // =========================
    // deleteModule
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class DeleteModule {

        @Test
        void deleteModule_shouldCorrectlySoftDeleteModuleAdnShiftPosition_whenValidRequest() {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module Test");
                m.setDescription("Description");
                m.setCourseId(courseId);
                m.setPosition(1);
            }));

            service.deleteModule(courseId, module.getId());

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

        @Test
        void deleteModule_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.deleteModule(courseId, moduleId)
            );

            assertAll(
                    () -> assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode())
            );
        }

    }

    // =========================
    // deleteAllModulesByCourseId
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class DeleteAllModulesByCourseId {

        @Test
        void deleteAllModulesByCourseId_shouldCorrectlySoftDeleteModulesInCourse_whenModulesExistsAndValidRequest() {
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

            service.deleteAllModulesByCourseId(courseId);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

    }

    // =========================
    // updatePosition
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class UpdatePosition {

        @Test
        void updatePosition_shouldCorrectlyChangePositionOfModuleAndShiftForAnotherInRange_whenValidRequest() {
            Long courseId = 1L;

            for (int i = 1; i <= 3; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            ModuleResponse response = service.updatePosition(courseId, 2L, request);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertFalse(result.isEmpty()),

                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(3, result.getTotalElements()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals("Module 1", result.getContent().getFirst().getName()),

                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals("Module 3", result.getContent().get(1).getName()),

                    () -> assertEquals(2, result.getContent().get(2).getPosition()),
                    () -> assertEquals("Module 2", result.getContent().get(2).getName())
            );
        }

        @Test
        void updatePosition_shouldCorrectlyChangePositionOfModuleAndUpForAnotherInRange_whenValidRequest() {
            Long courseId = 1L;

            for (int i = 1; i <= 3; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            ReorderModuleRequest request = new ReorderModuleRequest(1);

            ModuleResponse response = service.updatePosition(courseId, 3L, request);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertFalse(result.isEmpty()),

                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(3, result.getTotalElements()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals("Module 1", result.getContent().getFirst().getName()),

                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals("Module 3", result.getContent().get(1).getName()),

                    () -> assertEquals(2, result.getContent().get(2).getPosition()),
                    () -> assertEquals("Module 2", result.getContent().get(2).getName())
            );
        }

        @Test
        void updatePosition_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;

            ReorderModuleRequest request = new ReorderModuleRequest(1);

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.updatePosition(courseId, moduleId, request)
            );

            assertAll(
                    () -> assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode())
            );
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {
                -1, 10
        })
        void updatePosition_shouldThrowException_whenNewPositionIsInvalid(Integer position) {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                        m.setPosition(2);
                    })
            );

            ReorderModuleRequest request = new ReorderModuleRequest(position);

            AppException ex = assertThrows(
                    AppException.class,
                    () -> service.updatePosition(courseId, module.getId(), request)
            );

            assertAll(
                    () -> assertEquals(ErrorCode.MODULE_POSITION_INVALID, ex.getCode())
            );
        }

    }

}
