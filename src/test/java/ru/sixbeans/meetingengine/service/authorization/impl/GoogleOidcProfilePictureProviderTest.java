package ru.sixbeans.meetingengine.service.authorization.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.ActiveProfiles;
import ru.sixbeans.meetingengine.service.io.FileFetchingService;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class GoogleOidcProfilePictureProviderTest {

    @MockBean
    private FileFetchingService fileFetchingService;

    @Autowired
    private GoogleOidcProfilePictureProvider provider;

    private OidcUser user;

    @BeforeEach
    public void setUp() {
        Map<String, Object> claims = Map.of(
                "picture", "https://example.com/avatar?s128-c",
                "iss", "https://accounts.google.com",
                "sub", "1234567890"
        );
        OidcIdToken idToken = new OidcIdToken("tokenValue", Instant.now(), Instant.now().plusSeconds(60), claims);
        OidcUserInfo userInfo = new OidcUserInfo(claims);
        user = new DefaultOidcUser(Collections.emptyList(), idToken, userInfo);
    }

    @Test
    void testGetProfilePicture() {
        String expectedUrl = "https://example.com/avatar?s128-c";
        byte[] expectedData = new byte[]{1, 2, 3};

        when(fileFetchingService.get(expectedUrl)).thenReturn(Optional.of(expectedData));

        Optional<byte[]> result = provider.getProfilePicture(user);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedData);
    }

    @Test
    void testSupports() {
        assertThat(provider.supports(user)).isTrue();
    }
}
