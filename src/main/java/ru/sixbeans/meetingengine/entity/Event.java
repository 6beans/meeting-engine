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
    private String eventName;

    private String eventDescription;

    @ManyToMany
    private Set<Tag> tags;

    @OneToOne(cascade = CascadeType.ALL)
    private User eventOwner;

    @ManyToMany
    private Set<User> members;

    @Column(columnDefinition = "Date")
    private LocalDate eventCreateDate;

    @Column(columnDefinition = "Date")
    private LocalDate eventEndDate;
}
