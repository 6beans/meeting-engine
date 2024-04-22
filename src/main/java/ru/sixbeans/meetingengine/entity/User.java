package ru.sixbeans.meetingengine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private String sub;

    @Size(min = 4, max = 40)
    @Column(nullable = false)
    private String fullName;

    @Size(min = 2, max = 20)
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Lob
    private byte[] avatar;

    @Column(columnDefinition = "DATE")
    private LocalDate memberSince;

    @Column(nullable = false)
    private Boolean profileCompleted;

    @Column
    @Size(max = 3000)
    private String profileDescription;

    @Column
    @Size(min=1, max=64)
    private String telegramLink;

    @Column
    @Size(min=1, max=255)
    private String vkLink;

    // Связываем User'a с таблицей тегов
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "users_tags",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags;

    // Связываем User'a с таблицей users_acquaintances, которая отвечает за связь пользователя с людьми, которых он лайкнул
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "users_acquaintances",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "friend_id") })
    private Set<User> friends;
}
