package ru.sixbeans.meetingengine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String provider;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String userName;

    private String email;

    @Lob
    private byte[] avatar;

    @Column(columnDefinition = "DATE")
    private LocalDate memberSince;

    private String profileDescription;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private UserContacts userContacts;

    @ManyToMany
    @JoinTable(name = "users_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Event> owned;

    @ManyToMany(mappedBy = "members")
    private Set<Event> participated;
}
