package com.example.mercado.courses.moduleContent.repository;

import com.example.mercado.courses.moduleContent.entity.ModuleResource;
import com.example.mercado.courses.moduleContent.enums.ModuleResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleResourceRepository extends JpaRepository<ModuleResource, Long> {

    List<ModuleResource> findAllByModuleIdOrderByPositionAsc(Long moduleId);

    Optional<ModuleResource> findByModuleIdAndPosition(Long moduleId, Integer position);
    Optional<ModuleResource> findByModuleIdAndId(Long moduleId, Long id);
    Optional<ModuleResource> findByModuleIdAndNameAndType(Long moduleId, String name, ModuleResourceType type);

    boolean existsByModuleIdAndName(Long moduleId, String name);
    boolean existsByModuleIdAndNameAndType(Long moduleId, String name, ModuleResourceType type);
    boolean existsByModuleIdAndPosition(Long moduleId, Integer position);

    Integer countByModuleId(Long moduleId);

}
