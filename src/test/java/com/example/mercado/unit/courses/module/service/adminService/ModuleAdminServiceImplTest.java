package com.example.mercado.unit.courses.module.service.adminService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ReorderModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.module.service.adminService.ModuleAdminServiceImpl;
import com.example.mercado.courses.utils.EntityFinder;
import com.example.mercado.testUtils.courses.module.ModuleTestData;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("Module Admin Service Impl Test")
public class ModuleAdminServiceImplTest {

    @Mock
    private ModuleRepository repository;

    @Mock
    private ModuleMapper mapper;

    private ModuleAdminServiceImpl service;

    @BeforeEach
    public void setUp() {
        EntityFinder finder = new EntityFinder();
        service = new ModuleAdminServiceImpl(
                repository, mapper, finder
        );
    }


    // =========================
    // createModule
    // =========================
    @Nested
    class CreateModule {

        @Test
        void createModule_shouldCreateModule_whenValidRequest() {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("New name");
                m.setDescription("New description");
            });

            when(repository.existsByNameAndCourseId(request.name(), courseId))
                    .thenReturn(false);
            when(mapper.toEntity(request, courseId))
                    .thenReturn(module);
            when(mapper.toResponse(any(Module.class)))
                    .thenAnswer(i -> {
                        Module m = i.getArgument(0);
                        return ModuleTestData.mapToResponse(m);
                    });
            when(repository.save(any(Module.class)))
                    .thenAnswer(i -> i.getArgument(0));

            ModuleResponse result = service.createModule(courseId, request);

            verify(repository, times(1)).existsByNameAndCourseId(request.name(), courseId);
            verify(repository, times(1)).findMaxPositionByCourseId(courseId);
            verify(mapper, times(1)).toEntity(request, courseId);
            verify(mapper, times(1)).toResponse(module);
        }

        @Test
        void createModule_shouldCorrectlySetFirstPosition_whenNoOneModuleExists() {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("New name");
                m.setDescription("New description");
            });

            when(repository.existsByNameAndCourseId(request.name(), courseId))
                    .thenReturn(false);
            when(repository.findMaxPositionByCourseId(courseId))
                    .thenReturn(null);
            when(mapper.toEntity(request, courseId))
                    .thenReturn(module);
            when(mapper.toResponse(any(Module.class)))
                    .thenAnswer(i -> {
                        Module m = i.getArgument(0);
                        return ModuleTestData.mapToResponse(m);
                    });
            when(repository.save(any(Module.class)))
                    .thenAnswer(i -> i.getArgument(0));

            ModuleResponse result = service.createModule(courseId, request);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(0, module.getPosition())
            );
        }

        @Test
        void createModule_shouldCorrectlySetPosition_whenSomeModulesExists() {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("New name");
                m.setDescription("New description");
            });

            when(repository.existsByNameAndCourseId(request.name(), courseId))
                    .thenReturn(false);
            when(repository.findMaxPositionByCourseId(courseId))
                    .thenReturn(5);
            when(mapper.toEntity(request, courseId))
                    .thenReturn(module);
            when(mapper.toResponse(any(Module.class)))
                    .thenAnswer(i -> {
                        Module m = i.getArgument(0);
                        return ModuleTestData.mapToResponse(m);
                    });
            when(repository.save(any(Module.class)))
                    .thenAnswer(i -> i.getArgument(0));

            ModuleResponse result = service.createModule(courseId, request);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(6, module.getPosition())
            );
        }

        @Test
        void createModule_shouldThrowException_whenModuleAlreadyExistsByNameAndCourseId() {
            Long courseId = 1L;
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("New name");
                m.setDescription("New description");
            });

            when(repository.existsByNameAndCourseId(request.name(), courseId))
                    .thenReturn(true);

            AppException ex = Assertions.assertThrows(AppException.class, () -> {
                service.createModule(courseId, request);
            });

            Assertions.assertEquals(ErrorCode.MODULE_ALREADY_EXISTS, ex.getCode());

            verify(repository, times(1)).existsByNameAndCourseId(request.name(), courseId);
            verify(repository, never()).findMaxPositionByCourseId(courseId);
            verify(mapper, never()).toEntity(request, courseId);
            verify(mapper, never()).toResponse(module);
        }

    }

    // =========================
    // updateModule
    // =========================
    @Nested
    class UpdateModule {

        @Test
        void updateModule_shouldCorrectlyUpdateModule_whenValidRequest() {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("Old name");
                m.setDescription("Old description");
            });

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.of(module));
            when(repository.existsByNameAndCourseId(request.name(), courseId))
                    .thenReturn(false);
            when(mapper.toResponse(any(Module.class)))
                    .thenAnswer(i -> {
                        Module m = i.getArgument(0);
                        return ModuleTestData.mapToResponse(m);
                    });

            ModuleResponse response = service.updateModule(courseId, moduleId, request);

            Assertions.assertAll(
                    () -> Assertions.assertEquals("New name", module.getName()),
                    () -> Assertions.assertEquals("New description", module.getDescription()),
                    () -> verify(repository, times(1)).existsByNameAndCourseId(request.name(), courseId),
                    () -> verify(mapper, times(1)).toResponse(module)
            );
        }

        @Test
        void updateModule_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("Old name");
                m.setDescription("Old description");
            });

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.empty());

            AppException ex = Assertions.assertThrows(AppException.class, () -> {
                service.updateModule(courseId, moduleId, request);
            });
            Assertions.assertAll(
                    () -> Assertions.assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode()),
                    () -> verify(repository, never()).existsByNameAndCourseId(request.name(), courseId),
                    () -> verify(mapper, never()).toResponse(module)
            );
        }

        @Test
        void updateModule_shouldThrowException_whenModuleAlreadyExistsByNameAndCourseId() {
            Long courseId = 1L;
            Long moduleId = 2L;
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New name";
                m.description = "New description";
            });
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("Old name");
                m.setDescription("Old description");
            });

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.of(module));
            when(repository.existsByNameAndCourseId(request.name(), courseId))
                    .thenReturn(true);

            AppException ex = Assertions.assertThrows(AppException.class, () -> {
                service.updateModule(courseId, moduleId, request);
            });

            Assertions.assertAll(
                    () -> Assertions.assertEquals(ErrorCode.MODULE_ALREADY_EXISTS, ex.getCode()),
                    () -> verify(repository, times(1)).findByIdAndCourseId(moduleId, courseId),
                    () -> verify(repository, times(1)).existsByNameAndCourseId(request.name(), courseId),
                    () -> verify(mapper, never()).toResponse(module)
            );
        }

    }

    // =========================
    // deleteModule
    // =========================
    @Nested
    class DeleteModule {

        @Test
        void deleteModule_shouldCorrectlySoftDeleteModule_whenValidRequest() {
            Long courseId = 1L;
            Long moduleId = 2L;

            Module module = ModuleTestFactory.createModule(m -> {
                m.setPosition(5);
            });

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.of(module));
            when(repository.findMaxPositionByCourseId(courseId))
                    .thenReturn(10);

            service.deleteModule(courseId, moduleId);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(11, module.getPosition()),
                    () -> verify(repository, times(1)).findByIdAndCourseId(moduleId, courseId),
                    () -> verify(repository, times(1)).findMaxPositionByCourseId(courseId),
                    () -> verify(repository, times(1)).softDeleteByIdAndCourseId(moduleId, courseId)
            );
        }

        @Test
        void deleteModule_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.empty());

            AppException ex = Assertions.assertThrows(AppException.class, () -> {
                service.deleteModule(courseId, moduleId);
            });

            Assertions.assertAll(
                    () -> Assertions.assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode()),
                    () -> verify(repository, times(1)).findByIdAndCourseId(moduleId, courseId),
                    () -> verify(repository, never()).findMaxPositionByCourseId(courseId),
                    () -> verify(repository, never()).softDeleteByIdAndCourseId(moduleId, courseId)
            );
        }

    }

    // =========================
    // deleteAllModulesByCourseId
    // =========================
    @Nested
    class DeleteAllModulesByCourseId {

        @Test
        void deleteAllModulesByCourseId_shouldCorrectlySoftDeleteAllModulesByCourseId_whenValidRequest() {
            Long courseId = 1L;

            service.deleteAllModulesByCourseId(courseId);

            verify(repository).softDeleteAllByCourseId(courseId);
            verifyNoMoreInteractions(repository);
        }

    }

    // =========================
    // updatePosition
    // =========================
    @Nested
    class UpdatePosition {

        @Test
        void updatePosition_shouldCorrectlyUpdatePosition_whenValidRequest() {
            Long courseId = 1L;
            Long moduleId = 10L;

            Module module = ModuleTestFactory.createModule(m -> {
                m.setPosition(5);
            });

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.of(module));
            when(repository.findMaxPositionByCourseId(courseId))
                    .thenReturn(10);
            when(mapper.toResponse(any(Module.class)))
                    .thenAnswer(i -> {
                        Module m = i.getArgument(0);
                        return ModuleTestData.mapToResponse(m);
                    });

            service.updatePosition(courseId, moduleId, request);

            Assertions.assertAll(
                    () -> verify(repository).findByIdAndCourseId(moduleId, courseId),
                    () -> verify(repository).findMaxPositionByCourseId(courseId),
                    () -> verify(repository).incrementPositionRange(courseId, 2, 4),
                    () -> verify(repository).updatePosition(moduleId, 2),
                    () -> verifyNoMoreInteractions(repository)
            );
        }

        @Test
        void updatePosition_shouldThrowException_whenModuleNotFound() {
            Long courseId = 1L;
            Long moduleId = 2L;

            Module module = ModuleTestFactory.createModule(m -> {
                m.setPosition(5);
            });
            ReorderModuleRequest request = new ReorderModuleRequest(2);

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.empty());

            AppException ex = Assertions.assertThrows(AppException.class, () -> {
                service.updatePosition(courseId, moduleId, request);
            });

            Assertions.assertAll(
                    () -> verify(repository, times(1)).findByIdAndCourseId(moduleId, courseId),
                    () -> verify(repository, never()).findMaxPositionByCourseId(courseId),
                    () -> verify(repository, never()).incrementPositionRange(courseId, 2, 4),
                    () -> verify(repository, never()).updatePosition(moduleId, 2),
                    () -> verifyNoMoreInteractions(repository)
            );
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {
                -1,
                11
        })
        void updatePosition_shouldThrowException_whenPositionInvalid(Integer position) {
            Long courseId = 1L;
            Long moduleId = 10L;

            Module module = ModuleTestFactory.createModule(m -> {
                m.setPosition(5);
            });
            ReorderModuleRequest request = new ReorderModuleRequest(position);

            when(repository.findByIdAndCourseId(moduleId, courseId))
                    .thenReturn(Optional.of(module));

            when(repository.findMaxPositionByCourseId(courseId))
                    .thenReturn(10);

            AppException ex = Assertions.assertThrows(
                    AppException.class,
                    () -> service.updatePosition(courseId, moduleId, request)
            );

            Assertions.assertAll(
                    () -> Assertions.assertEquals(ErrorCode.MODULE_POSITION_INVALID, ex.getCode()),
                    () -> verify(repository).findByIdAndCourseId(moduleId, courseId),
                    () -> verify(repository).findMaxPositionByCourseId(courseId),
                    () -> verifyNoMoreInteractions(repository)
            );
        }

    }

}
