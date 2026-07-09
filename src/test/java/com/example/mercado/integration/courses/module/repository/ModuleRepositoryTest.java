package com.example.mercado.integration.courses.module.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Module Repository Integration Test")
public class ModuleRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ModuleRepository repository;

    // =========================
    // findByIdAndCourseId
    // =========================
    @Nested
    class FindByIdAndCourseId {

        @Test
        void findByIdAndCourseId_shouldReturnModule_whenModuleExists() {
            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(1L);
                    })
            );

            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(1L);
                    })
            );

            Optional<Module> result = repository.findByIdAndCourseId(
                    module_one.getId(), module_one.getCourseId()
            );

            assertAll(
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(module_one.getId(), result.get().getId()),
                    () -> assertEquals(module_one.getCourseId(), result.get().getCourseId()),
                    () -> assertEquals(module_one.getName(), result.get().getName())
            );
        }

        @Test
        void findByIdAndCourseId_shouldReturnOptionalOfEmpty_whenModulesDoesNotExist() {
            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(1L);
                    })
            );

            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(1L);
                    })
            );

            Optional<Module> result = repository.findByIdAndCourseId(
                    3L, module_one.getCourseId()
            );

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

        @Test
        void findByIdAndCourseId_shouldReturnOptionalOfEmpty_whenCourseIdDoesNotMatch() {
            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(1L);
                    })
            );

            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(1L);
                    })
            );

            Optional<Module> result = repository.findByIdAndCourseId(
                    module_one.getId(), 2L
            );

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

    }

    // =========================
    // existsByNameAndCourseId
    // =========================
    @Nested
    class ExistsByNameAndCourseId {

        @Test
        void existsByNameAndCourseId_shouldReturnTrue_whenModuleExists() {
            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(1L);
                    })
            );
            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(1L);
                    })
            );

            boolean result = repository.existsByNameAndCourseId(
                    module_one.getName(), module_one.getCourseId()
            );

            assertAll(
                    () -> assertTrue(result)
            );
        }

        @Test
        void existsByNameAndCourseId_shouldReturnFalse_whenDoesNotExist() {
            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(1L);
                    })
            );
            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(1L);
                    })
            );

            boolean result = repository.existsByNameAndCourseId(
                    "Module 3", module_one.getCourseId()
            );

            assertAll(
                    () -> assertFalse(result)
            );
        }

        @Test
        void existsByNameAndCourseId_shouldReturnFalse_whenCourseIdDoesNotMatch() {
            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(1L);
                    })
            );
            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(1L);
                    })
            );

            boolean result = repository.existsByNameAndCourseId(
                    module_one.getName(), 2L
            );

            assertAll(
                    () -> assertFalse(result)
            );
        }

    }

    // =========================
    // findAllByCourseIdAndDeletedFalse
    // =========================
    @Nested
    class FindAllByCourseIdAndDeletedFalse {

        @Test
        void findAllByCourseIdAndDeletedFalse_shouldReturnPageWithModules_whenModulesExist() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages())
            );
        }

        @Test
        void findAllByCourseIdAndDeletedFalse_shouldIgnoreDeletedModules() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }
            repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module deleted");
                        m.setCourseId(courseId);
                        m.setDeleted(true);
                    })
            );

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages())
            );
        }

        @Test
        void findAllByCourseIdAndDeletedFalse_shouldIgnoreModulesFromAnotherCourse() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }
            repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module from another course");
                        m.setCourseId(2L);
                    })
            );

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages())
            );
        }

        @Test
        void findAllByCourseIdAndDeletedFalse_shouldReturnEmptyPage_whenDoesNotExist() {
            Long courseId = 1L;

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(0, result.getTotalElements()),
                    () -> assertEquals(0, result.getTotalPages())
            );
        }

        @Test
        void findAllByCourseIdAndDeletedFalse_shouldReturnOrderedPageByPosition() {
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

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals("Module 1", result.getContent().getFirst().getName()),

                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals("Module 2", result.getContent().get(1).getName())
            );
        }

    }

    // =========================
    // countByCourseIdAndDeletedFalse
    // =========================
    @Nested
    class CountByCourseIdAndDeletedFalse {

        @Test
        void countByCourseIdAndDeletedFalse_shouldReturnCorrectNumberOfModules_whenModulesExist() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }

            long result = repository.countByCourseIdAndDeletedFalse(courseId);

            assertAll(
                    () -> assertEquals(5, result)
            );
        }

        @Test
        void countByCourseIdAndDeletedFalse_shouldReturnZero_whenModulesDoesNotExist() {
            Long courseId = 1L;

            long result = repository.countByCourseIdAndDeletedFalse(courseId);

            assertAll(
                    () -> assertEquals(0, result)
            );
        }

        @Test
        void countByCourseIdAndDeletedFalse_shouldNotCountModulesFromAnotherCourse() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }
            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(2L);
                        })
                );
            }

            long result = repository.countByCourseIdAndDeletedFalse(courseId);

            assertAll(
                    () -> assertEquals(5, result)
            );
        }

    }

    // =========================
    // softDeleteAllByCourseId
    // =========================
    @Nested
    class SoftDeleteAllByCourseId {

        @Test
        void softDeleteAllByCourseId_shouldMarkAllModulesInCourseAsDeleted_whenModulesExist() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }

            repository.softDeleteAllByCourseId(courseId);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

        @Test
        void softDeleteAllByCourseId_shouldNotAffectModulesFromOtherCourse_whenModulesExist() {
            Long courseId = 1L;

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                        })
                );
            }
            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(2L);
                        })
                );
            }

            repository.softDeleteAllByCourseId(courseId);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

    }

    // =========================
    // softDeleteByIdAndCourseId
    // =========================
    @Nested
    class SoftDeleteByIdAndCourseId {

        @Test
        void softDeleteByIdAndCourseId_shouldMarkModuleInCourseAsDeleted_whenModuleExists() {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                    })
            );

            repository.softDeleteByIdAndCourseId(module.getId(), courseId);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertTrue(result.isEmpty())
            );
        }

        @Test
        void softDeleteByIdAndCourseId_shouldNotAffectModulesFromOtherCourse_whenModuleExists() {
            Long courseId = 1L;

            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                    })
            );
            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(2L);
                    })
            );

            repository.softDeleteByIdAndCourseId(module_one.getId(), courseId);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertTrue(result.isEmpty()),
                    () -> assertFalse(module_two.isDeleted())
            );
        }

        @Test
        void softDeleteByIdAndCourseId_shouldDoNothing_whenCourseIdIsNotMatched() {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                    })
            );

            repository.softDeleteByIdAndCourseId(module.getId(), 2L);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertFalse(result.isEmpty()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(1, result.getTotalElements()),

                    () -> assertEquals("Module", result.getContent().getFirst().getName()),
                    () -> assertEquals(courseId, result.getContent().getFirst().getCourseId()),
                    () -> assertFalse(result.getContent().getFirst().isDeleted())
            );
        }

    }

    // =========================
    // findMaxPositionByCourseId
    // =========================
    @Nested
    class FindMaxPositionByCourseId {

        @Test
        void findMaxPositionByCourseId_shouldReturnNumberOfLastPosition_whenModulesExist() {
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

            Integer result = repository.findMaxPositionByCourseId(courseId);

            assertAll(
                    () -> assertEquals(4, result)
            );
        }

        @Test
        void findMaxPositionByCourseId_shouldReturnMinusOne_whenDoesNotExist() {
            Long courseId = 1L;

            Integer result = repository.findMaxPositionByCourseId(courseId);

            assertAll(
                    () -> assertEquals(-1, result)
            );
        }

    }

    // =========================
    // updatePosition
    // =========================
    @Nested
    class UpdatePosition {

        @Test
        void updatePosition_shouldUpdateModulePosition_whenModuleExists() {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                        m.setPosition(1);
                    })
            );

            repository.updatePosition(module.getId(), 3);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertFalse(result.isEmpty()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(1, result.getTotalElements()),

                    () -> assertEquals(3, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(courseId, result.getContent().getFirst().getCourseId()),
                    () -> assertEquals("Module", result.getContent().getFirst().getName())
            );
        }

        @Test
        void updatePosition_shouldNotUpdateAnything_whenModuleDoesNotExist() {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                        m.setPosition(1);
                    })
            );

            repository.updatePosition(2L, 3);

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertFalse(result.isEmpty()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(1, result.getTotalElements()),

                    () -> assertEquals(1, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(courseId, result.getContent().getFirst().getCourseId()),
                    () -> assertEquals("Module", result.getContent().getFirst().getName())
            );
        }

        @Test
        void updatePosition_shouldNotChangeOtherModules_whenUpdatingOneModule() {
            Long courseId = 1L;

            Module module_one = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 1");
                        m.setCourseId(courseId);
                        m.setPosition(1);
                    })
            );
            Module module_two = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module 2");
                        m.setCourseId(courseId);
                        m.setPosition(2);
                    })
            );

            repository.updatePosition(module_one.getId(), 3);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").descending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertFalse(result.isEmpty()),
                    () -> assertEquals(1, result.getTotalPages()),
                    () -> assertEquals(2, result.getTotalElements()),

                    () -> assertEquals(3, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(courseId, result.getContent().getFirst().getCourseId()),
                    () -> assertEquals("Module 1", result.getContent().getFirst().getName()),

                    () -> assertEquals(2, result.getContent().get(1).getPosition()),
                    () -> assertEquals(courseId, result.getContent().get(1).getCourseId()),
                    () -> assertEquals("Module 2", result.getContent().get(1).getName())
            );
        }

    }

    // =========================
    // incrementPositionRange
    // =========================
    @Nested
    class IncrementPositionRange {

        @Test
        void incrementPositionRange_shouldIncrementPositions_whenModulesInRangeExist() {
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

            repository.incrementPositionRange(courseId, 2, 4);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(3, result.getContent().get(2).getPosition()),
                    () -> assertEquals(4, result.getContent().get(3).getPosition()),
                    () -> assertEquals(5, result.getContent().get(4).getPosition())
            );
        }

        @Test
        void incrementPositionRange_shouldNotUpdateModulesOutsideRange_whenModulesExistOutsideRange() {
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

            repository.incrementPositionRange(courseId, 2, 7);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(3, result.getContent().get(2).getPosition()),
                    () -> assertEquals(4, result.getContent().get(3).getPosition()),
                    () -> assertEquals(5, result.getContent().get(4).getPosition())
            );
        }

        @Test
        void incrementPositionRange_shouldNotUpdateModulesFromAnotherCourse_whenCourseHasSamePositions() {
            Long courseId = 1L;
            Long anotherCourseId = 2L;

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

            for (int i = 1; i <= 5; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(anotherCourseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            repository.incrementPositionRange(anotherCourseId, 2, 4);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);
            Page<Module> resultForAnotherCourse = repository.findAllByCourseIdAndDeletedFalse(anotherCourseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(2, result.getContent().get(2).getPosition()),
                    () -> assertEquals(3, result.getContent().get(3).getPosition()),
                    () -> assertEquals(4, result.getContent().get(4).getPosition()),

                    () -> assertEquals(0, resultForAnotherCourse.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, resultForAnotherCourse.getContent().get(1).getPosition()),
                    () -> assertEquals(3, resultForAnotherCourse.getContent().get(2).getPosition()),
                    () -> assertEquals(4, resultForAnotherCourse.getContent().get(3).getPosition()),
                    () -> assertEquals(5, resultForAnotherCourse.getContent().get(4).getPosition())
            );
        }

        @Test
        void incrementPositionRange_shouldNotUpdateAnything_whenNoModulesInRangeExist() {
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

            repository.incrementPositionRange(courseId, 5, 7);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(5, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(2, result.getContent().get(2).getPosition()),
                    () -> assertEquals(3, result.getContent().get(3).getPosition()),
                    () -> assertEquals(4, result.getContent().get(4).getPosition())
            );
        }

    }

    // =========================
    // decrementPositionRange
    // =========================
    @Nested
    class DecrementPositionRange {

        @Test
        void decrementPositionRange_shouldDecrementPositions_whenModulesInRangeExist() {
            Long courseId = 1L;

            repository.saveAll(
                    List.of(
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 1");
                                m.setCourseId(courseId);
                                m.setPosition(1);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 2");
                                m.setCourseId(courseId);
                                m.setPosition(2);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 3");
                                m.setCourseId(courseId);
                                m.setPosition(3);
                            })
                    )
            );

            repository.decrementPositionRange(courseId, 1, 3);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(3, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(2, result.getContent().get(2).getPosition())
            );
        }

        @Test
        void decrementPositionRange_shouldNotUpdateModulesOutsideRange_whenModulesExistOutsideRange() {
            Long courseId = 1L;

            repository.saveAll(
                    List.of(
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 1");
                                m.setCourseId(courseId);
                                m.setPosition(1);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 2");
                                m.setCourseId(courseId);
                                m.setPosition(2);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 3");
                                m.setCourseId(courseId);
                                m.setPosition(3);
                            })
                    )
            );

            repository.decrementPositionRange(courseId, 1, 2);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(3, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(3, result.getContent().get(2).getPosition())
            );
        }

        @Test
        void decrementPositionRange_shouldNotUpdateModulesFromAnotherCourse_whenCourseHasSamePositions() {
            Long courseId = 1L;
            Long anotherCourseId = 2L;

            repository.saveAll(
                    List.of(
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 1");
                                m.setCourseId(courseId);
                                m.setPosition(1);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 2");
                                m.setCourseId(courseId);
                                m.setPosition(2);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 3");
                                m.setCourseId(courseId);
                                m.setPosition(3);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 1");
                                m.setCourseId(anotherCourseId);
                                m.setPosition(1);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 2");
                                m.setCourseId(anotherCourseId);
                                m.setPosition(2);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 3");
                                m.setCourseId(anotherCourseId);
                                m.setPosition(3);
                            })
                    )
            );

            repository.decrementPositionRange(courseId, 1, 3);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);
            Page<Module> resultFromAnotherCourse = repository.findAllByCourseIdAndDeletedFalse(anotherCourseId, pageable);

            assertAll(
                    () -> assertEquals(3, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(0, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(1, result.getContent().get(1).getPosition()),
                    () -> assertEquals(2, result.getContent().get(2).getPosition()),

                    () -> assertEquals(1, resultFromAnotherCourse.getContent().getFirst().getPosition()),
                    () -> assertEquals(2, resultFromAnotherCourse.getContent().get(1).getPosition()),
                    () -> assertEquals(3, resultFromAnotherCourse.getContent().get(2).getPosition())
            );
        }

        @Test
        void decrementPositionRange_shouldNotUpdateAnything_whenNoModulesInRangeExist() {
            Long courseId = 1L;

            repository.saveAll(
                    List.of(
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 1");
                                m.setCourseId(courseId);
                                m.setPosition(1);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 2");
                                m.setCourseId(courseId);
                                m.setPosition(2);
                            }),
                            ModuleTestFactory.createModule(m -> {
                                m.setName("Module 3");
                                m.setCourseId(courseId);
                                m.setPosition(3);
                            })
                    )
            );

            repository.decrementPositionRange(courseId, 4, 6);

            Pageable pageable = PageRequest.of(0, 10, Sort.by("position").ascending());
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertAll(
                    () -> assertEquals(3, result.getTotalElements()),
                    () -> assertEquals(1, result.getTotalPages()),

                    () -> assertEquals(1, result.getContent().getFirst().getPosition()),
                    () -> assertEquals(2, result.getContent().get(1).getPosition()),
                    () -> assertEquals(3, result.getContent().get(2).getPosition())
            );
        }

    }

}
