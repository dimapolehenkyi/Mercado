package com.example.mercado.unit.courses.module.service.publicService;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.ModuleShortResponse;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.module.service.publicService.ModulePublicServiceImpl;
import com.example.mercado.courses.utils.EntityFinder;
import com.example.mercado.testUtils.courses.module.ModuleTestData;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(
        MessageConfig.class
)
@DisplayName("Module Public Service Impl Test")
public class ModulePublicServiceImplTest {

    @Mock
    private ModuleRepository repository;

    @Mock
    private ModuleMapper mapper;

    private ModulePublicServiceImpl service;

    @BeforeEach
    public void setUp() {
        EntityFinder finder = new EntityFinder();
        service = new ModulePublicServiceImpl(
                repository, mapper, finder
        );
    }

    // =========================
    // getModuleById
    // =========================
    @Nested
    class GetModuleById {

        @Test
        void getModuleById_shouldReturnCorrectModule_whenExists() {
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("Test module");
                m.setDescription("Test description");
                m.setCourseId(1L);
            });

            ModuleResponse response = ModuleTestData.mapToResponse(module);

            when(repository.findByIdAndCourseId(1L, 1L))
                    .thenReturn(Optional.of(module));
            when(mapper.toResponse(module))
                    .thenReturn(response);

            ModuleResponse result = service.getModuleById(1L, 1L);

            Assertions.assertEquals(response, result);
        }

        @Test
        void getModuleById_shouldThrowException_whenNotExists() {
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("Test module");
                m.setDescription("Test description");
                m.setCourseId(1L);
            });

            when(repository.findByIdAndCourseId(1L, 1L))
                    .thenThrow(
                            new AppException(ErrorCode.MODULE_NOT_FOUND)
                    );

            AppException ex = Assertions.assertThrows(
                    AppException.class,
                    () -> service.getModuleById(1L, 1L)
            );

            Assertions.assertEquals(ErrorCode.MODULE_NOT_FOUND, ex.getCode());
        }

    }

    // =========================
    // getAllModulesByCourseId
    // =========================
    @Nested
    class GetAllModulesByCourseId {

        @Test
        void getAllModulesByCourseId_shouldReturnCorrectPages_whenExists() {
            Module module = ModuleTestFactory.createModule(m -> {
                m.setName("Test module");
                m.setDescription("Test description");
                m.setCourseId(1L);
            });

            ModuleShortResponse response = ModuleTestData.mapToShortResponse(module);

            Pageable pageable = PageRequest.of(0, 10);

            Page<Module> modulePage = new PageImpl<>(List.of(module));

            when(repository.findAllByCourseIdAndDeletedFalse(1L, pageable))
                    .thenReturn(modulePage);
            when(mapper.toShortResponse(module))
                    .thenReturn(response);

            Page<ModuleShortResponse> result =
                    service.getAllModulesByCourseId(1L, pageable);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(1, result.getTotalElements()),
                    () -> Assertions.assertEquals("Test module", result.getContent().getFirst().name())
            );
        }

        @Test
        void getAllModulesByCourseId_shouldReturnEmptyPage_whenNoOneExists() {
            Pageable pageable = PageRequest.of(0, 10);

            Page<Module> emptyPage = new PageImpl<>(Collections.emptyList());

            when(repository.findAllByCourseIdAndDeletedFalse(1L, pageable))
                    .thenReturn(emptyPage);

            Page<ModuleShortResponse> result =
                    service.getAllModulesByCourseId(1L, pageable);

            Assertions.assertTrue(result.isEmpty());

            verify(mapper, never()).toShortResponse(any());
        }

    }

    // =========================
    // countAllModules
    // =========================
    @Nested
    class CountAllModules {

        @Test
        void countAllModules_shouldReturnCorrectNumberOfModules_whenExists() {
            when(repository.countByCourseIdAndDeletedFalse(1L))
                    .thenReturn(5L);

            long result = service.countAllModules(1L);

            Assertions.assertEquals(5L, result);
        }

        @Test
        void countAllModules_shouldReturnZero_whenModulesNotExists() {
            when(repository.countByCourseIdAndDeletedFalse(1L))
                    .thenReturn(0L);

            long result = service.countAllModules(1L);

            Assertions.assertEquals(0L, result);
        }

    }

}
