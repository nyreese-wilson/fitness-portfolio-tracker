package com.example.fitnesstracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "exercise_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_log_id", nullable = false)
    @JsonBackReference
    private WorkoutLog workoutLog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_def_id", nullable = false)
    private ExerciseDefinition exerciseDefinition;

    private Integer sets;
    private Integer reps;

    @Column(name = "weight_lbs", precision = 5, scale = 2)
    private BigDecimal weightLbs;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;
}

