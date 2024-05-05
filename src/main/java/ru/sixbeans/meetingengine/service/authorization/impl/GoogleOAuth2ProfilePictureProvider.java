package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.service.authorization.OAuth2ProfilePictureProvider;
import ru.sixbeans.meetingengine.service.io.FileFetchingService;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2ProfilePictureProvider implements OAuth2ProfilePictureProvider {

    private final FileFetchingService fileFetchingService;

    @Override
    public boolean supports(OAuth2AuthenticationToken token) {
        return "google".equals(token.getAuthorizedClientRegistrationId());
    }

    @Override
    public Optional<byte[]> getProfilePicture(OAuth2AuthenticationToken token) {
        OAuth2User user = token.getPrincipal();
        String url = Objects.requireNonNull(user.getAttribute("picture"));
        String adjustedUrl = url + (url.contains("?") ? "&" : "?") + "sz=512";
        return fetchPicture(adjustedUrl);
    }

    private Optional<byte[]> fetchPicture(String url) {
        return fileFetchingService.get(url);
    }
}
