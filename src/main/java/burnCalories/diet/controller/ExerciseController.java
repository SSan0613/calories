package burnCalories.diet.controller;

import burnCalories.diet.DTO.exercise.ResponseStats;
import burnCalories.diet.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/stats")
    public ResponseStats getStats() {
        return exerciseService.getStats();
    }
}
