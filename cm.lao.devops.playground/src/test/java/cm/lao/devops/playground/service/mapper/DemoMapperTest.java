package cm.lao.devops.playground.service.mapper;

import cm.lao.common.microservice.common.service.mapper.UuidMapperImpl;
import cm.lao.common.microservice.common.util.Constants;
import cm.lao.devops.playground.domain.Demo;
import cm.lao.devops.playground.dto.DemoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DemoMapperTest {
    DemoMapper objectUnderTest;

    @BeforeEach
    void setUp() {
        objectUnderTest = new DemoMapperImpl(new UuidMapperImpl());
    }

    @Test
    void toDtoCode1Test() {
        var name = "demo";
        Demo demo = Demo.builder()
                .name(name)
                .build();
        var demoDTO = objectUnderTest.toDto(
                demo,
                Constants.FIELDS_TO_EXTRACT_1
        );
        assertThat(demoDTO)
                .isNotNull()
                .returns(demo.getId().toUuid(), DemoDTO::getId)
                .returns(name, DemoDTO::getName);
    }
}