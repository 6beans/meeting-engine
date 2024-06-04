package ru.sixbeans.meetingengine.model;

import java.util.Arrays;
import java.util.Objects;

public record UserProfileData(AccountData account,
                              byte[] picture) {

    public record AccountData(
            String userName,
            String firstName,
            String secondName,
            String email,
            String about
    ) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfileData that)) return false;
        return Objects.deepEquals(picture, that.picture) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, Arrays.hashCode(picture));
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
               "account=" + account +
               ", picture=" + Arrays.toString(picture) +
               '}';
    }
}
