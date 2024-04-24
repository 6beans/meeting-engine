package ru.sixbeans.meetingengine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany
    private Set<Tag> tags;

    @OneToOne(cascade = CascadeType.ALL)
    private User owner;

    @ManyToMany
    private Set<User> members;

    @Column(columnDefinition = "Date")
    private LocalDate createDate;

    @Column(columnDefinition = "Date")
    private LocalDate endDate;
}
