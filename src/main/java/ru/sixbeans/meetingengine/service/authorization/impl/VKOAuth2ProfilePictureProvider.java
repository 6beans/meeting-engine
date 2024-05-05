package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.service.FileFetchingService;
import ru.sixbeans.meetingengine.service.authorization.OAuth2ProfilePictureProvider;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VKOAuth2ProfilePictureProvider implements OAuth2ProfilePictureProvider {

    private final FileFetchingService fileFetchingService;

    @Override
    public boolean supports(OAuth2AuthenticationToken token) {
        return "vk".equals(token.getAuthorizedClientRegistrationId());
    }

    @Override
    public Optional<byte[]> getProfilePicture(OAuth2AuthenticationToken token) {
        OAuth2User user = token.getPrincipal();
        String url = user.getAttribute("photo_max_orig");
        return fetchPicture(url);
    }

    private Optional<byte[]> fetchPicture(String url) {
        return fileFetchingService.get(url);
    }
}
