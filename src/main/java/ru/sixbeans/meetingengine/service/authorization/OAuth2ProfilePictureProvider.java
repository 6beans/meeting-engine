package ru.sixbeans.meetingengine.service.authorization;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Optional;

public interface OAuth2ProfilePictureProvider {

    boolean supports(OAuth2AuthenticationToken token);

    Optional<byte[]> getProfilePicture(OAuth2AuthenticationToken token);
}
