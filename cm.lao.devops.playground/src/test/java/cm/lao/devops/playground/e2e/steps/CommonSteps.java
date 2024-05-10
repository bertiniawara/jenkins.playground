package cm.lao.devops.playground.e2e.steps;

import cm.lao.common.microservice.e2e.LastRequestResponse;
import cm.lao.common.microservice.e2e.MockJwtDecoder;
import cm.lao.devops.playground.e2e.HttpClient;
import io.cucumber.java8.En;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class CommonSteps implements En {
    private static final MockJwtDecoder JWT_DECODER = new MockJwtDecoder();
    private MockedStatic<JwtDecoders> jwtDecodersMockedStatic;
    @Autowired
    private HttpClient httpClient;
    @Value("${wiremock.server.port}")
    private int wiremockPort;
    @Autowired
    private OutputDestination outputDestination;

    public CommonSteps() {
        Before(() -> {
                    stubFor(
                            get(urlEqualTo("/tenant-config/1"))
                                    .willReturn(
                                            aResponse()
                                                    .withStatus(200)
                                                    .withBody(
                                                            Files.readString(
                                                                    Path.of(requireNonNull(
                                                                            HttpClient.class.getResource(
                                                                                    "tenant_1.json"
                                                                            )
                                                                    ).toURI())
                                                            ).replaceFirst(
                                                                    "https://auth.lao-sarl.cm",
                                                                    "http://localhost:" + wiremockPort
                                                            )
                                                    )
                                                    .withHeader(
                                                            HttpHeaders.CONTENT_TYPE,
                                                            MediaType.APPLICATION_JSON_VALUE
                                                    )
                                    )
                    );
                    jwtDecodersMockedStatic = mockStatic(JwtDecoders.class);
                    jwtDecodersMockedStatic.when(() -> JwtDecoders.fromIssuerLocation(anyString()))
                            .thenReturn(JWT_DECODER);
                }
        );

        After(
                () -> {
                    outputDestination.clear();
                    reset();
                    jwtDecodersMockedStatic.close();
                }
        );

        And(
                "I am connected as user {string} with the scope(s) {string}",
                (String username, String commaSeparatedScopes) ->
                        JWT_DECODER
                                .setToken("token")
                                .setUsername(username)
                                .setScopes(commaSeparatedScopes.replaceAll(",", " "))
        );

        And(
                "The last request failed with the http status {string} and error code {string}",
                (String httpStatus, String errorCode) ->
                        assertThat(httpClient.getLastResponse()).isEqualTo(
                                new LastRequestResponse(
                                        HttpStatus.valueOf(httpStatus.toUpperCase()),
                                        errorCode
                                )
                        )
        );

        And("I wait for {int} milliseconds", (Integer arg0) -> Thread.sleep(arg0));

        And("I activate the scheduler named {string}",
                (String name) -> {
                    httpClient
                            .getWebTestClient()
                            .put()
                            .uri(
                                    uriBuilder -> uriBuilder
                                            .path("/scheduler/{name}")
                                            .queryParam("enable", true)
                                            .build(Map.of("name", name))
                            )
                            .exchange();
                    Thread.sleep(2000);

                });
    }

}
