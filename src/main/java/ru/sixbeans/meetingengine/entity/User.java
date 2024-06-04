package ru.sixbeans.meetingengine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String issuer;

    @Column(unique = true, nullable = false)
    private String subject;

    @Lob
    private byte[] avatar;

    @Builder.Default
    @ManyToMany(mappedBy = "users")
    private Set<Tag> tags = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_id")
    )
    private Set<User> subscriptions = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "members")
    private Set<Event> memberEvents = new HashSet<>();
}
