package ru.sixbeans.meetingengine.service.authorization.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class JpaOidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication.getPrincipal() instanceof OidcUser principal) {
            String subject = principal.getSubject();
            if (!userRepository.existsById(subject)) {
                User user = new User();
                user.setId(subject);
                userRepository.save(user);
            }
        } else throw new IllegalStateException("Unsupported authentication type: " + authentication);
        response.sendRedirect("/");
    }
}
