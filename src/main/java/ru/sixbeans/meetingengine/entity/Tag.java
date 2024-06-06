package ru.sixbeans.meetingengine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @NotBlank
    @Column(nullable = false)
    private String id;

    @Builder.Default
    @ManyToMany(mappedBy = "tags")
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private Set<Event> events = new HashSet<>();
}
