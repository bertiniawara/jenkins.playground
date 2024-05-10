package cm.lao.tenant.e2e.steps;

import cm.lao.common.microservice.e2e.LastRequestResponse;
import cm.lao.tenant.dto.DemoDTO;
import cm.lao.tenant.e2e.HttpClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;
import java.util.UUID;

import static cm.lao.common.microservice.common.util.Constants.FIELDS_TO_EXTRACT_CODE;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DemoSteps implements En {
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private List<DemoDTO> demos;

    public DemoSteps() {
        And(
                "I assume that the demos with the following data are inside the database",
                (DataTable dataTable) -> {
                    var dataInTheDatabase = jdbcTemplate.query(
                            "SELECT * FROM t_demo",
                            (rs, rowNum) -> new DemoDTO()
                                    .id(UUID.fromString(rs.getString("c_id")))
                                    .name(rs.getString("c_name"))
                    );
                    assertThat(dataInTheDatabase)
                            .hasSizeGreaterThanOrEqualTo(2)
                            .containsAll(
                                    dataTable
                                            .asMaps()
                                            .stream()
                                            .map(
                                                    data -> new DemoDTO()
                                                            .id(UUID.fromString(data.get("id")))
                                                            .name(data.get("name"))
                                            )
                                            .toList()
                            );
                }
        );

        And(
                "I fetch all demos using the extraction code {string}",
                (String fieldsExtractionCode) -> this.demos = httpClient
                        .get(
                                uriBuilder -> uriBuilder
                                        .path("/demo")
                                        .queryParam(FIELDS_TO_EXTRACT_CODE, fieldsExtractionCode)
                                        .build()
                        )
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(DemoDTO.class)
                        .returnResult()
                        .getResponseBody()
        );

        And(
                "I should see that the following data are among the fetched ones",
                (DataTable dataTable) -> assertThat(demos)
                        .containsAll(
                                dataTable
                                        .asMaps()
                                        .stream()
                                        .map(
                                                data -> new DemoDTO()
                                                        .id(UUID.fromString(data.get("id")))
                                                        .name(data.get("name"))
                                        )
                                        .toList()
                        )
        );

        And(
                "I try to fetch all demos using the extraction code {string}",
                (String fieldsToExtractCode) -> {
                    EntityExchangeResult<String> result = httpClient
                            .get("/demo")
                            .exchange()
                            .expectBody(String.class)
                            .returnResult();
                    httpClient.setLastResponse(
                            new LastRequestResponse(
                                    result.getStatus(),
                                    result.getResponseBody()
                            )
                    );
                }
        );
        And(
                "I should see that the following data are not among the fetched ones",
                (DataTable dataTable) -> assertThat(demos)
                        .map(Object.class::cast)
                        .doesNotContain(
                                dataTable
                                        .asMaps()
                                        .stream()
                                        .map(
                                                data -> new DemoDTO()
                                                        .id(UUID.fromString(data.get("id")))
                                                        .name(data.get("name"))
                                        )
                                        .toArray()
                        )
        );
    }
}
