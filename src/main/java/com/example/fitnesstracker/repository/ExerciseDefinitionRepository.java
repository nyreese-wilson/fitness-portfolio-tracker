package com.example.fitnesstracker.repository;
import com.example.fitnesstracker.model.ExerciseDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseDefinitionRepository extends JpaRepository<ExerciseDefinition, Integer> {}
