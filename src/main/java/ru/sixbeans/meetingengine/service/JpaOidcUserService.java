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

import static java.time.LocalDate.now;

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
        String host = oidcUser.getIssuer().getHost();
        String sub = oidcUser.getSubject();
        String name = oidcUser.getFullName();
        String email = oidcUser.getEmail();

        String photoUrl = oidcUser.getPicture();
        photoUrl = photoUrl.substring(0, photoUrl.indexOf('=') + 1) + "h512-c";
        var avatar = fileFetchingService.get(photoUrl);

        sub = "%s-%s".formatted(host, sub);

        if (!userRepository.existsBySub(sub)) {
            User user = new User();
            user.setSub(sub);
            user.setEmail(email);
            user.setFullName(name);
            user.setMemberSince(now());
            user.setUserName("" + sub.hashCode());
            avatar.ifPresent(user::setAvatar);
            user.setProfileCompleted(false);
            userRepository.save(user);
        }

        return oidcUser;
    }
}
