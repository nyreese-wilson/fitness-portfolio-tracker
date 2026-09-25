package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.*;
import com.example.fitnesstracker.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutLogRepository workoutLogRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ExerciseDefinitionRepository exerciseDefinitionRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private User sampleUser;
    private ExerciseDefinition sampleDefinition;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1, "fit_dev", "dev@test.com", null);
        sampleDefinition = new ExerciseDefinition(1, "Bench Press", "Strength");
    }

    @Test
    @DisplayName("logWorkout should successfully save workout and establish bidirectional relationship")
    void logWorkout_Success() {
        WorkoutLog workoutData = new WorkoutLog();
        workoutData.setNotes("Chest day");
        workoutData.setExerciseLogs(new ArrayList<>());

        ExerciseLog logLine = new ExerciseLog();
        logLine.setExerciseDefinition(sampleDefinition);
        logLine.setSets(4);
        logLine.setReps(10);
        logLine.setWeightLbs(new BigDecimal("135.00"));
        workoutData.getExerciseLogs().add(logLine);

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(exerciseDefinitionRepository.findById(1)).thenReturn(Optional.of(sampleDefinition));
        when(workoutLogRepository.save(any(WorkoutLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WorkoutLog result = workoutService.logWorkout(1, workoutData);

        assertNotNull(result);
        assertEquals(sampleUser, result.getUser());
        assertEquals(LocalDate.now(), result.getWorkoutDate());
        assertEquals(1, result.getExerciseLogs().size());
        assertEquals(result, result.getExerciseLogs().get(0).getWorkoutLog());
        verify(workoutLogRepository, times(1)).save(workoutData);
    }

    @Test
    @DisplayName("logWorkout should throw IllegalArgumentException when User ID does not exist")
    void logWorkout_UserNotFound() {
        WorkoutLog workoutData = new WorkoutLog();
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workoutService.logWorkout(99, workoutData);
        });

        assertTrue(exception.getMessage().contains("User with ID 99 not found"));
        verify(workoutLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("logWorkout should throw IllegalArgumentException when Exercise Definition ID does not exist")
    void logWorkout_ExerciseDefNotFound() {
        WorkoutLog workoutData = new WorkoutLog();
        workoutData.setExerciseLogs(new ArrayList<>());

        ExerciseLog logLine = new ExerciseLog();
        ExerciseDefinition invalidDef = new ExerciseDefinition();
        invalidDef.setId(404);
        logLine.setExerciseDefinition(invalidDef);
        workoutData.getExerciseLogs().add(logLine);

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(exerciseDefinitionRepository.findById(404)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workoutService.logWorkout(1, workoutData);
        });

        assertTrue(exception.getMessage().contains("Exercise Definition ID 404 not found"));
        verify(workoutLogRepository, never()).save(any());
    }
}

