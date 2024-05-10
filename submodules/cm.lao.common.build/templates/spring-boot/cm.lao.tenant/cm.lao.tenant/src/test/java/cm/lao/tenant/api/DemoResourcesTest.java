package cm.lao.tenant.api;

import cm.lao.common.microservice.api.ResourcesTest;
import cm.lao.tenant.dto.DemoDTO;
import cm.lao.tenant.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static cm.lao.common.microservice.common.util.Constants.FIELDS_TO_EXTRACT_1;
import static cm.lao.common.microservice.common.util.Constants.FIELDS_TO_EXTRACT_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DemoResourcesTest extends ResourcesTest {
    @MockBean
    private DemoService demoService;

    @Override
    protected Object getResources() {
        return new DemoResources(demoService);
    }

    @Test
    void fetchAllDemoTest() {
        var demoDTO1 = new DemoDTO().id(UUID.randomUUID());
        var demoDTO2 = new DemoDTO().id(UUID.randomUUID());

        when(demoService.fetchAll(FIELDS_TO_EXTRACT_1)).thenReturn(List.of(demoDTO1, demoDTO2));

        var responseBody = webTestClient
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/demo")
                                .queryParam(FIELDS_TO_EXTRACT_CODE, FIELDS_TO_EXTRACT_1)
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DemoDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody)
                .hasSize(2)
                .contains(demoDTO1, demoDTO2);
    }
}