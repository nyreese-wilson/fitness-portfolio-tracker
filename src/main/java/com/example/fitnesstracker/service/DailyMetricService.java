package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.DailyMetric;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.repository.DailyMetricRepository;
import com.example.fitnesstracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DailyMetricService {

    private final DailyMetricRepository dailyMetricRepository;
    private final UserRepository userRepository;

    public DailyMetricService(DailyMetricRepository dailyMetricRepository, UserRepository userRepository) {
        this.dailyMetricRepository = dailyMetricRepository;
        this.userRepository = userRepository;
    }

    public DailyMetric logDailyMetric(Integer userId, DailyMetric metricData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        metricData.setUser(user);

        if (metricData.getLogDate() == null) {
            metricData.setLogDate(LocalDate.now());
        }

        return dailyMetricRepository.save(metricData);
    }

    public List<DailyMetric> getUserMetrics(Integer userId) {
        return dailyMetricRepository.findByUserId(userId);
    }
}

