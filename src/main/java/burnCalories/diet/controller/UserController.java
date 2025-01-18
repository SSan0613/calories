package burnCalories.diet.controller;

import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseTodayLogDTO;
import burnCalories.diet.DTO.userDTO.userinfo.ResponseUserInfoDTO;
import burnCalories.diet.DTO.userDTO.userinfo.UpdateUserInfoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import burnCalories.diet.DTO.userDTO.exerciseLog.ExerciseLogDTO;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;
import burnCalories.diet.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    //테스트
    @GetMapping("/debug")
    public ResponseEntity<Object> debugAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication);
    }

    //마이페이지 정보 호출
    @GetMapping("/info")
    public ResponseEntity<ResponseUserInfoDTO> getInfo(@AuthenticationPrincipal String username) {
        User user = userRepository.findByUsername(username).get();

        log.info(user.getUsername(), user.getNickname());
        return ResponseEntity.ok(new ResponseUserInfoDTO(user.getNickname(), user.getEmail(), user.getHeight(), user.getWeight()));
    }

    //정보 수정
    @PutMapping("/info")
    public ResponseEntity<String> changeInfo(@AuthenticationPrincipal String username, @RequestBody @Valid UpdateUserInfoDTO updateUserInfoDTO) {
        userService.changeInfo(username, updateUserInfoDTO);

        return ResponseEntity.ok("회원정보 수정 완료");
    }

    //오늘의 운동량 조회
    @GetMapping("/logs")
    public ResponseEntity<List<ResponseTodayLogDTO>> getTodayExerciseLog() {
        List<ResponseTodayLogDTO> todayExerciseLog = userService.getTodayExerciseLog();
        return ResponseEntity.ok(todayExerciseLog);
    }
    //운동 기록하기
    @PostMapping("/logs")
    public ResponseEntity<String> postExerciseLog(@AuthenticationPrincipal String username, @RequestBody @Valid ExerciseLogDTO exerciseLog) {
        try {
            userService.validateDate(exerciseLog);

            userService.putExerciseLog(username, exerciseLog);
            return ResponseEntity.ok("운동 기록 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //운동 기록 수정
    @PutMapping("/logs/{id}")
    public ResponseEntity<String> changeExerciseLog(@PathVariable Long id, @RequestBody @Valid ExerciseLogDTO exerciseLogDTO) {
        log.info(String.valueOf(id));
        userService.changeExerciseLog(id,exerciseLogDTO);

        return ResponseEntity.ok("운동 기록 수정 완료");
    }

    //운동 기록 삭제
    @DeleteMapping("/logs/{id}")
    public ResponseEntity<String> deleteExerciseLog(@PathVariable Long id) {
        log.info(String.valueOf(id));
        userService.deleteExerciseLog(id);

        return ResponseEntity.ok("운동 기록 삭제 완료");
    }
    //누적 운동량
    @GetMapping("/cumulative")
    public ResponseEntity<List<?>> getWeeksExercise(@AuthenticationPrincipal String username, @RequestParam(required = false) int weeks, @RequestParam String type) {

        List<?> result = new ArrayList<>();
        if (type.equals("duration")) {
            result = userService.getDurationLog(weeks, username);
        } else if (type.equals("calories")) {
            result = userService.getCaloriesLog(weeks, username);
        }
        return ResponseEntity.ok(result);
    }


}
