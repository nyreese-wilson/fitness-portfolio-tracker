package com.example.fitnesstracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_metrics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "log_date"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Architectural Decision: Many metrics belong to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "calories_consumed")
    private Integer caloriesConsumed;

    @Column(name = "weight_lbs", precision = 5, scale = 2)
    private BigDecimal weightLbs;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
