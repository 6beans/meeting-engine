package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import ru.sixbeans.meetingengine.service.authorization.OidcProfilePictureProvider;
import ru.sixbeans.meetingengine.service.io.FileFetchingService;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GoogleOidcProfilePictureProvider implements OidcProfilePictureProvider {

    private final FileFetchingService fileFetchingService;

    @Override
    public boolean supports(OidcUser user) {
        return "accounts.google.com"
                .equals(user.getIssuer().getHost());
    }

    @Override
    public Optional<byte[]> getProfilePicture(OidcUser user) {
        String url = Objects.requireNonNull(user.getPicture());
        String basePart = url.split("=s96-c")[0];
        String adjustedUrl = basePart + "=s512-c";
        return fetchPicture(adjustedUrl);
    }

    private Optional<byte[]> fetchPicture(String url) {
        return fileFetchingService.get(url);
    }
}
