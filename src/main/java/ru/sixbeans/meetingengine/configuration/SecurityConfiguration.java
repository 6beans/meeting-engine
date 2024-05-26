package ru.sixbeans.meetingengine.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import static org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 259200) // 3 days
public class SecurityConfiguration {

    private final AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/webjars/**")
                        .permitAll().anyRequest().authenticated())
                .sessionManagement(s -> s.maximumSessions(1))
                .oauth2Login(oauth2 -> oauth2.loginPage("/login")
                        .userInfoEndpoint(u -> u.oidcUserService(oidcUserService()))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler()))
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .csrf(c -> c.csrfTokenRepository(withHttpOnlyFalse()));

        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            log.error("Authentication failure: {}", exception.getMessage(), exception);
            response.sendRedirect("/login?error");
        };
    }
}
