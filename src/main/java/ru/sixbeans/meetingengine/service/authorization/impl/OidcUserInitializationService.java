package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.exception.UserNotFoundException;
import ru.sixbeans.meetingengine.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OidcUserInitializationService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUserIfNotExist(OidcUser principal) {
        String issuer = principal.getIssuer().getHost();
        String subject = principal.getSubject();
        if (userRepository.existsByIssuerAndSubject(issuer, subject)) {
            return userRepository.findByIssuerAndSubject(issuer, subject)
                    .orElseThrow(() -> new UserNotFoundException("%s:%s".formatted(issuer, subject)))
                    .getId();
        }

        principal.getClaims().entrySet().forEach(System.out::println);

        User user = new User();
        user.setIssuer(issuer);
        user.setSubject(subject);

        userRepository.save(user);
        return user.getId();
    }
}
