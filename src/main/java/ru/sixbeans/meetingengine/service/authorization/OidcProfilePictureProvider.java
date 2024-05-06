package ru.sixbeans.meetingengine.service.authorization;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

public interface OidcProfilePictureProvider {

    boolean supports(OidcUser user);

    Optional<byte[]> getProfilePicture(OidcUser user);
}
