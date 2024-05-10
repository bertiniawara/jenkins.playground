package cm.lao.tenant.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class OAuth2Security {

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    @ConditionalOnProperty(name = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(httpSecurityCsrfConfigurer -> {})
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/demo"
                                ).hasAuthority(Scopes.DEMO_READ_ALL)
                                .anyRequest().denyAll()
                )
                .oauth2ResourceServer(
                        httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                                .authenticationManagerResolver(authenticationManagerResolver)
                )
                .build();
    }

    @ConditionalOnProperty(name = "spring.security.enabled", havingValue = "false")
    @Bean
    public SecurityFilterChain disabledSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> {
                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .anyRequest()
                                .permitAll()
                )
                .build();
    }

    @ConditionalOnProperty(name = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return (webSecurity) -> webSecurity
                .ignoring()
                .requestMatchers("/actuator/health/**")
                .requestMatchers("/actuator/swagger-ui");
    }

}
