package com.judit.catshelter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int friendship;

    @Enumerated(EnumType.STRING)
    private CatMood mood;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus adoptionStatus;

    private Instant lastFedAt;
    private Instant lastPettedAt;
    private Instant lastPlayedAt;
}
