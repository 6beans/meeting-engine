package ru.sixbeans.meetingengine.service.impl.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.entity.UserContacts;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JpaOAuth2UserService implements AuthenticationSuccessHandler {

    private final OAuth2ProfilePictureService profilePictureService;
    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) createUserIfNotExist(token);
        else throw new IllegalStateException("Unsupported authentication type");
        response.sendRedirect("/");
    }

    private void createUserIfNotExist(OAuth2AuthenticationToken token) {
        var provider = token.getAuthorizedClientRegistrationId();
        var oAuth2User = token.getPrincipal();
        var externalId = oAuth2User.getName();

        if (userRepository
                .findByProviderAndExternalId(provider, externalId)
                .isPresent()) return;

        var profilePicture = profilePictureService.getProfilePicture(token);

        User user = new User();

        user.setProvider(provider);
        user.setUserName(externalId);
        user.setExternalId(externalId);
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setFullName(oAuth2User.getAttribute("name"));
        user.setMemberSince(LocalDate.now());
        profilePicture.ifPresent(user::setAvatar);

        UserContacts userContacts = new UserContacts();
        userContacts.setUser(user);

        user.setUserContacts(userContacts);

        userRepository.save(user);
    }
}
