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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

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
                userRepository.save(User.builder()
                        .avatar(defaultAvatar())
                        .id(subject).build());
            }
        } else throw new IllegalStateException("Unsupported authentication type: " + authentication);
        response.sendRedirect("/");
    }

    private byte[] defaultAvatar() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream("static/default.png")) {
            return Objects.requireNonNull(is).readAllBytes();
        }
    }
}
