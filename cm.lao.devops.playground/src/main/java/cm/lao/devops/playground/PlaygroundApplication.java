package cm.lao.devops.playground;

import cm.lao.common.microservice.caching.hazelcast.HazelcastAutoConfiguration;
import cm.lao.common.microservice.common.Application;
import cm.lao.common.microservice.common.CommonAutoConfiguration;
import cm.lao.common.microservice.encryption.EncryptionAutoConfiguration;
import cm.lao.common.microservice.external_services.ExternalServiceHelperAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EntityScan(basePackages = {"cm.lao.devops.playground.domain"})
@EnableJpaRepositories(basePackages = {"cm.lao.devops.playground.repository"})
@EnableWebSecurity
@EnableDiscoveryClient
@Import({
		CommonAutoConfiguration.class,
		HazelcastAutoConfiguration.class,
		ExternalServiceHelperAutoConfiguration.class,
		EncryptionAutoConfiguration.class
})
@SpringBootApplication(
		exclude = {
				UserDetailsServiceAutoConfiguration.class,
				ErrorMvcAutoConfiguration.class
		}
)
public class PlaygroundApplication extends Application {

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundApplication.class, args);
	}

	@Override
	protected void started() {
	}

	@Override
	protected void stopped() {
	}

}
