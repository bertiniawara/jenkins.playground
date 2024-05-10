package cm.lao.tenant.e2e;

import cm.lao.common.microservice.e2e.BaseHttpClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class HttpClient extends BaseHttpClient {

    public HttpClient(MockMvc mockMvc) {
        super(mockMvc);
    }
}

