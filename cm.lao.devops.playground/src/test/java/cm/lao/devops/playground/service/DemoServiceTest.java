package cm.lao.devops.playground.service;

import cm.lao.common.microservice.common.util.Constants;
import cm.lao.devops.playground.domain.Demo;
import cm.lao.devops.playground.dto.DemoDTO;
import cm.lao.devops.playground.repository.DemoRepository;
import cm.lao.devops.playground.service.mapper.DemoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoServiceTest {

    DemoService objectUnderTest;
    @Mock
    DemoRepository demoRepository;
    @Mock
    DemoMapper demoMapper;

    @BeforeEach
    void setUp() {
        objectUnderTest = new DemoService(demoRepository, demoMapper);
    }

    @Test
    void fetchAllTest() {
        var demo1 = mock(Demo.class);
        var demo2 = mock(Demo.class);
        when(demoRepository.findAll()).thenReturn(List.of(demo1, demo2));

        var demoDTO1 = mock(DemoDTO.class);
        var demoDTO2 = mock(DemoDTO.class);
        var fieldsExtractionCode = Constants.FIELDS_TO_EXTRACT_1;
        when(demoMapper.toDto(demo1, fieldsExtractionCode)).thenReturn(demoDTO1);
        when(demoMapper.toDto(demo2, fieldsExtractionCode)).thenReturn(demoDTO2);

        var demoDTOS = objectUnderTest.fetchAll(fieldsExtractionCode);

        assertThat(demoDTOS)
                .hasSize(2)
                .contains(demoDTO1)
                .contains(demoDTO2);
    }
}