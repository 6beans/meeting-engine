package ru.sixbeans.meetingengine.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.sixbeans.meetingengine.model.UserData;

import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final Keycloak keycloak;
    private final UserService userService;

    public boolean isUsernameUnique(String id, String username) {
        return keycloak.realm("engine").users()
                .search(username, true).stream()
                .map(UserRepresentation::getId)
                .allMatch(id::equals);
    }

    public boolean isEmailUnique(String id, String email) {
        return keycloak.realm("engine").users()
                .searchByEmail(email, true).stream()
                .filter(UserRepresentation::isEmailVerified)
                .map(UserRepresentation::getId)
                .allMatch(id::equals);
    }

    public void updateUserProfile(String id, UserData userData) {
        var resource = keycloak.realm("engine").users().get(id);
        var representation = resource.toRepresentation();
        // User Info
        representation.setUsername(userData.userName());
        representation.setFirstName(userData.firstName());
        representation.setLastName(userData.secondName());
        representation.singleAttribute("about", userData.about());
        // User Email
        representation.setEmail(userData.email());
        representation.setEmailVerified(false);
        resource.update(representation);
        resource.sendVerifyEmail();
    }

    public void updateUserPassword(String id, String password) {
        var resource = keycloak.realm("engine").users().get(id);
        var credential = new CredentialRepresentation();
        credential.setType(PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        resource.resetPassword(credential);
    }

    public void updateUserAvatar(String subject, byte[] bytes) {
        userService.updateUserAvatar(subject, bytes);
    }
}
