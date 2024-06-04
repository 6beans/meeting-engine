package ru.sixbeans.meetingengine.configuration;

import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@SuppressWarnings("unused")
@ConfigurationProperties("keycloak")
public class KeycloakConfiguration {

    private final String password;
    private final String username;
    private final String serverUrl;
    private final String logoutUrl;

    public KeycloakConfiguration(String realm, String password, String username, String serverUrl) {
        this.password = password;
        this.username = username;
        this.serverUrl = serverUrl;
        logoutUrl = "%s/realms/%s/protocol/openid-connect/logout"
                .formatted(serverUrl, realm);
    }

    @Bean
    public Keycloak keycloak() {
        System.out.println(logoutUrl);
        return KeycloakBuilder.builder().serverUrl(serverUrl)
                .realm("master").clientId("admin-cli")
                .username(username).password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
