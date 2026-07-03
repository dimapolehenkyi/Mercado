package com.example.mercado.unit.courses.module.mapper;

import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.mapper.ModuleMapperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Module Mapper Test")
@ExtendWith(SpringExtension.class)
@Import(ModuleMapperImpl.class)
public class ModuleMapperTest {

    @Mock
    private final ModuleMapper mapper = Mappers.getMapper(ModuleMapper.class);

}
