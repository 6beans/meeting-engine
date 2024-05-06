package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import ru.sixbeans.meetingengine.service.authorization.OidcProfilePictureProvider;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OidcProfilePictureService {

    private final List<OidcProfilePictureProvider> providers;

    public Optional<byte[]> getProfilePicture(OidcUser principal) {
        return providers.stream()
                .filter(p -> p.supports(principal)).findFirst()
                .flatMap(p -> p.getProfilePicture(principal));
    }
}
