package cm.lao.devops.playground.e2e;

import cm.lao.common.microservice.e2e.HazelcastInitializer;
import cm.lao.common.microservice.e2e.WireMockTestConfiguration;
import cm.lao.devops.playground.PlaygroundApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Slf4j
@AutoConfigureWireMock(port = 0)
@RequiredArgsConstructor
@AutoConfigureMockMvc
@ActiveProfiles({"e2e"})
@SpringJUnitConfig
@CucumberContextConfiguration
@ContextConfiguration(
        initializers = {HazelcastInitializer.class}
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                PlaygroundApplication.class,
                TestChannelBinderConfiguration.class,
                WireMockTestConfiguration.class
        }
)
public class CucumberSpringConfiguration {
}

