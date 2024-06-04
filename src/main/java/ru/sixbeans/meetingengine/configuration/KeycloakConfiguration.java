package ru.sixbeans.meetingengine.configuration;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@ConfigurationProperties("keycloak")
public class KeycloakConfiguration {

    private final String password;
    private final String username;
    private final String serverUrl;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder().serverUrl(serverUrl)
                .realm("master").clientId("admin-cli")
                .username(username).password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
