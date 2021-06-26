package com.kosticnikola.player.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "UPIN")
    @Getter
    @Setter
    private Long UPIN;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column(name = "date_of_birth")
    @Getter
    @Setter
    private LocalDate dateOfBirth;

    public Player(Long UPIN, String name, LocalDate dateOfBirth) {
        this.UPIN = UPIN;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }
}
