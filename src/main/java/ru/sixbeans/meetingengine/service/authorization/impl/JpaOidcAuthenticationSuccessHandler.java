package ru.sixbeans.meetingengine.service.authorization.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JpaOidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OidcProfilePictureService profilePictureService;
    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication.getPrincipal() instanceof OidcUser user) createUserIfNotExist(user);
        else throw new IllegalStateException("Unsupported authentication type");
        response.sendRedirect("/");
    }

    private void createUserIfNotExist(OidcUser principal) {
        String provider = principal.getIssuer().getHost();
        String subject = principal.getSubject();
        if (userRepository.existsByProviderAndSubject(provider, subject)) return;

        var profilePicture = profilePictureService
                .getProfilePicture(principal);

        User user = new User();

        user.setProvider(provider);
        user.setUserName(subject);
        user.setSubject(subject);
        user.setEmail(principal.getEmail());
        profilePicture.ifPresent(user::setAvatar);

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setFullName(principal.getFullName());
        personalInfo.setUser(user);

        user.setPersonalInfo(personalInfo);

        userRepository.save(user);
    }
}
