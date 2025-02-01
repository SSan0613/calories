package burnCalories.diet.controller;

import burnCalories.diet.DTO.exerciseDTO.ExerciseLogDTO;
import burnCalories.diet.DTO.exerciseDTO.RequestRecommendDTO;
import burnCalories.diet.DTO.mlDTO.MLRecommendDTO;
import burnCalories.diet.DTO.exerciseDTO.ResponseTodayLogDTO;
import burnCalories.diet.service.ExerciseService;
import burnCalories.diet.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ProgressService progressService;

    //오늘의 운동량 조회
    @GetMapping("/logs")
    public ResponseEntity<List<ResponseTodayLogDTO>> getTodayExerciseLog(@AuthenticationPrincipal String username) {
        List<ResponseTodayLogDTO> todayExerciseLog = exerciseService.getTodayExerciseLog(username);
        return ResponseEntity.ok(todayExerciseLog);
    }

    //운동 기록하기
    @PostMapping("/logs")
    public ResponseEntity<Map<String,Object>> postExerciseLog(@AuthenticationPrincipal String username, @RequestBody @Valid ExerciseLogDTO exerciseLog) {
        try {
            //운동 입력 시간 검증
            exerciseService.validateDate(exerciseLog);
            //운동 기록 저장
            double calories = exerciseService.putExerciseLog(username, exerciseLog);

            //챌린지 진행률 업데이트
            progressService.updateChallengePercent(username);
            return ResponseEntity.ok(Map.of("calories",calories));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //운동 기록 수정
    @PutMapping("/logs/{id}")
    public ResponseEntity<String> changeExerciseLog(@AuthenticationPrincipal String username, @PathVariable Long id, @RequestBody @Valid ExerciseLogDTO exerciseLogDTO) {
        log.info(String.valueOf(id));
        //운동 기록 수정
        exerciseService.changeExerciseLog(username, id, exerciseLogDTO);

        //챌린지 진행률 업데이트
        progressService.updateChallengePercent(username);
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
    public ResponseEntity<MLRecommendDTO> recommendExercise(@AuthenticationPrincipal String username, @RequestBody RequestRecommendDTO recommendDTO) {
        MLRecommendDTO responseRecommendDTO = exerciseService.recommendExercise(username, recommendDTO);
        return ResponseEntity.ok(responseRecommendDTO);
    }
}
