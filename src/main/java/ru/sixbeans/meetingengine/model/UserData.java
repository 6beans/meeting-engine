package ru.sixbeans.meetingengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData{
    private Long id;
    private String email;
    private String fullName;
    private String userName;
    private byte[] avatar;
    private String profileDescription;
    private String tgLink;
    private String vkLink;
    private Set<TagData> tags;
    private Set<FriendData> friends;
}