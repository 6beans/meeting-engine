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

@Component
@RequiredArgsConstructor
public class JpaOidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OidcUserService userService;

    @Override
    @SneakyThrows
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication.getPrincipal() instanceof OidcUser user) {
            Long userId = userService.createUserIfNotExist(user);
            request.getSession().setAttribute("userId", userId);
        } else {
            throw new IllegalStateException("Unsupported authentication type");
        }
        response.sendRedirect("/");
    }
}
