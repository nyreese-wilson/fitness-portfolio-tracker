package com.example.fitnesstracker.service;

import com.example.fitnesstracker.model.ExerciseLog;
import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.model.WorkoutLog;
import com.example.fitnesstracker.repository.ExerciseDefinitionRepository;
import com.example.fitnesstracker.repository.UserRepository;
import com.example.fitnesstracker.repository.WorkoutLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutLogRepository workoutLogRepository;
    private final UserRepository userRepository;
    private final ExerciseDefinitionRepository exerciseDefinitionRepository;

    public WorkoutService(WorkoutLogRepository workoutLogRepository,
                          UserRepository userRepository,
                          ExerciseDefinitionRepository exerciseDefinitionRepository) {
        this.workoutLogRepository = workoutLogRepository;
        this.userRepository = userRepository;
        this.exerciseDefinitionRepository = exerciseDefinitionRepository;
    }

    @Transactional
    public WorkoutLog logWorkout(Integer userId, WorkoutLog workoutData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        workoutData.setUser(user);

        if (workoutData.getWorkoutDate() == null) {
            workoutData.setWorkoutDate(LocalDate.now());
        }

        for (ExerciseLog log : workoutData.getExerciseLogs()) {
            log.setWorkoutLog(workoutData);

            Integer defId = log.getExerciseDefinition().getId();
            var exerciseDef = exerciseDefinitionRepository.findById(defId)
                    .orElseThrow(() -> new IllegalArgumentException("Exercise Definition ID " + defId + " not found"));
            log.setExerciseDefinition(exerciseDef);
        }

        return workoutLogRepository.save(workoutData);
    }

    public List<WorkoutLog> getUserWorkouts(Integer userId) {
        return workoutLogRepository.findByUserId(userId);
    }
}

