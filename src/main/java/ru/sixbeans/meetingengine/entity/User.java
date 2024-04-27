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
    private String signedSub;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String userName;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Lob
    private byte[] avatar;

    @Column(columnDefinition = "DATE")
    private LocalDate memberSince;

    @Column(nullable = false)
    private Boolean profileCompleted;

    private String profileDescription;

    @OneToOne(cascade = CascadeType.ALL)
    private UserContacts userContacts;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Event> owned;

    @ManyToMany
    @JoinTable(name = "events_members",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Event> participated;
}
