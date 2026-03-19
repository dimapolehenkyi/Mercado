package com.example.mercado.courses.moduleResource.repository;

import com.example.mercado.courses.moduleResource.entity.ModuleResource;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
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
