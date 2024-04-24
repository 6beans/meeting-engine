package ru.sixbeans.meetingengine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final FileFetchingService fileFetchingService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);
        updateUserDetails(oidcUser);
        return oidcUser;
    }

    private void updateUserDetails(OidcUser oidcUser) {
        String sub = generateSignedSub(oidcUser);
        if (!userRepository.existsBySignedSub(sub))
            createUser(oidcUser, sub);
    }

    private String generateSignedSub(OidcUser oidcUser) {
        String host = oidcUser.getIssuer().getHost();
        String sub = oidcUser.getSubject();
        return "%s-%s".formatted(host, sub);
    }

    private void createUser(OidcUser oidcUser, String signedSub) {
        String photoUrl = adjustPhotoUrl(oidcUser.getPicture());
        Optional<byte[]> avatar = fileFetchingService.get(photoUrl);

        User user = new User();
        user.setSignedSub(signedSub);
        user.setEmail(oidcUser.getEmail());
        user.setFullName(oidcUser.getFullName());
        user.setMemberSince(java.time.LocalDate.now());
        user.setUserName(oidcUser.getSubject());
        avatar.ifPresent(user::setAvatar);
        user.setProfileCompleted(false);

        userRepository.save(user);
    }

    private String adjustPhotoUrl(String photoUrl) {
        return photoUrl.substring(0, photoUrl.indexOf('=') + 1) + "h512-c";
    }
}
