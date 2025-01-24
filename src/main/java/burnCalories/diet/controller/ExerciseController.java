package burnCalories.diet.controller;

import burnCalories.diet.DTO.exerciseDTO.ExerciseLogDTO;
import burnCalories.diet.DTO.exerciseDTO.ResponseRecommendDTO;
import burnCalories.diet.DTO.exerciseDTO.ResponseTodayLogDTO;
import burnCalories.diet.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    //오늘의 운동량 조회
    @GetMapping("/logs")
    public ResponseEntity<List<ResponseTodayLogDTO>> getTodayExerciseLog() {
        List<ResponseTodayLogDTO> todayExerciseLog = exerciseService.getTodayExerciseLog();
        return ResponseEntity.ok(todayExerciseLog);
    }

    //운동 기록하기
    @PostMapping("/logs")
    public ResponseEntity<String> postExerciseLog(@AuthenticationPrincipal String username, @RequestBody @Valid ExerciseLogDTO exerciseLog) {
        try {
            exerciseService.validateDate(exerciseLog);

            exerciseService.putExerciseLog(username, exerciseLog);
            return ResponseEntity.ok("운동 기록 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //운동 기록 수정
    @PutMapping("/logs/{id}")
    public ResponseEntity<String> changeExerciseLog(@AuthenticationPrincipal String username, @PathVariable Long id, @RequestBody @Valid ExerciseLogDTO exerciseLogDTO) {
        log.info(String.valueOf(id));
        exerciseService.changeExerciseLog(username, id, exerciseLogDTO);

        return ResponseEntity.ok("운동 기록 수정 완료");
    }

    //운동 기록 삭제
    @DeleteMapping("/logs/{id}")
    public ResponseEntity<String> deleteExerciseLog(@PathVariable Long id) {
        log.info(String.valueOf(id));
        exerciseService.deleteExerciseLog(id);

        return ResponseEntity.ok("운동 기록 삭제 완료");
    }

    //누적 운동량
    @GetMapping("/cumulative")
    public ResponseEntity<List<?>> getWeeksExercise(@AuthenticationPrincipal String username, @RequestParam(required = false) int weeks, @RequestParam String type) {

        List<?> result = new ArrayList<>();
        if (type.equals("duration")) {
            result = exerciseService.getDurationLog(weeks, username);
        } else if (type.equals("calories")) {
            result = exerciseService.getCaloriesLog(weeks, username);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/recommend")
    public ResponseEntity<ResponseRecommendDTO> recommendExercise(@AuthenticationPrincipal String username, @RequestBody double duration) {
        ResponseRecommendDTO responseRecommendDTO = exerciseService.recommendExercise(username, duration);
        return ResponseEntity.ok(responseRecommendDTO);
    }
}
