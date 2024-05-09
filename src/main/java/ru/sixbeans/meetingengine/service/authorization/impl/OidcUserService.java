package ru.sixbeans.meetingengine.service.authorization.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.PersonalInfo;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OidcUserService {

    private final UserRepository userRepository;
    private final OidcProfilePictureService profilePictureService;

    @Transactional
    public Long createUserIfNotExist(OidcUser principal) {
        String provider = principal.getIssuer().getHost();
        String subject = principal.getSubject();
        if (userRepository.existsByProviderAndSubject(provider, subject)) {
            return userRepository.findByProviderAndSubject(provider, subject)
                    .orElseThrow(() -> new IllegalStateException("User not found after existence check"))
                    .getId();
        }

        var profilePicture = profilePictureService.getProfilePicture(principal);

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
        return user.getId();
    }
}
