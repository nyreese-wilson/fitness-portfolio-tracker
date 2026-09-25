package com.example.fitnesstracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_definitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;
}
