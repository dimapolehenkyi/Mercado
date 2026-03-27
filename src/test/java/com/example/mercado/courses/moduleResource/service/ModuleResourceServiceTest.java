package com.example.mercado.courses.moduleResource.service;

import com.example.mercado.courses.moduleResource.dto.CreateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.dto.ModuleResourceResponse;
import com.example.mercado.courses.moduleResource.dto.UpdateModuleResourceRequest;
import com.example.mercado.courses.moduleResource.entity.ModuleResource;
import com.example.mercado.courses.moduleResource.mapper.ModuleResourceMapper;
import com.example.mercado.courses.moduleResource.repository.ModuleResourceRepository;
import com.example.mercado.courses.moduleResource.service.interfaces.ModuleResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModuleResourceService Test")
public class ModuleResourceServiceTest {

    @Mock
    private ModuleResourceMapper mapper;

    @Mock
    private ModuleResourceRepository moduleResourceRepository;

    @InjectMocks
    private ModuleResourceServiceImpl service;


    private ModuleResource moduleResource;
    private CreateModuleResourceRequest createModuleResourceRequest;
    private UpdateModuleResourceRequest updateModuleResourceRequest;
    private ModuleResourceResponse moduleResourceResponse;


    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Func ")
    void createModuleResource_shouldCreateModuleResource_andReturnResponse() {}

    @Test
    @DisplayName("Func ")
    void updateModuleResource_shouldUpdateModuleResource_andReturnResponse() {}

    @Test
    @DisplayName("Func ")
    void getModuleResourceById_shouldReturnModuleResource_andReturnResponse() {}

    @Test
    @DisplayName("Func ")
    void deleteModuleResource_shouldDeleteModuleResource() {}

    @Test
    @DisplayName("Func ")
    void getAllModuleResources_shouldReturnList_ofModuleResource() {}

}
