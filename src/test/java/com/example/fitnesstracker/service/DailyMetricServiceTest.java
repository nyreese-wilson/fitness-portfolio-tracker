package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.DailyMetric;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.repository.DailyMetricRepository;
import com.example.fitnesstracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyMetricServiceTest {

    @Mock
    private DailyMetricRepository dailyMetricRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DailyMetricService dailyMetricService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1, "fit_dev", "dev@test.com", null);
    }

    @Test
    @DisplayName("logDailyMetric should save metrics accurately and inject a missing date default")
    void logDailyMetric_Success() {
        DailyMetric metricInput = new DailyMetric();
        metricInput.setCaloriesConsumed(2500);
        metricInput.setWeightLbs(new BigDecimal("180.50"));
        metricInput.setNotes("Felt recovered today");

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(dailyMetricRepository.save(any(DailyMetric.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DailyMetric savedMetric = dailyMetricService.logDailyMetric(1, metricInput);

        assertNotNull(savedMetric);
        assertEquals(sampleUser, savedMetric.getUser());
        assertEquals(LocalDate.now(), savedMetric.getLogDate());
        assertEquals(2500, savedMetric.getCaloriesConsumed());
        verify(dailyMetricRepository, times(1)).save(metricInput);
    }

    @Test
    @DisplayName("logDailyMetric should reject input when the target Profile User ID is invalid")
    void logDailyMetric_UserNotFound() {
        DailyMetric metricInput = new DailyMetric();
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dailyMetricService.logDailyMetric(99, metricInput);
        });

        assertTrue(exception.getMessage().contains("User with ID 99 not found"));
        verify(dailyMetricRepository, never()).save(any());
    }
}
