package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sixbeans.meetingengine.service.authorization.OAuth2ProfilePictureProvider;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2ProfilePictureService {

    private final List<OAuth2ProfilePictureProvider> providers;

    public Optional<byte[]> getProfilePicture(OAuth2AuthenticationToken token) {
        return providers.stream()
                .filter(p -> p.supports(token)).findFirst()
                .flatMap(p -> p.getProfilePicture(token));
    }
}
