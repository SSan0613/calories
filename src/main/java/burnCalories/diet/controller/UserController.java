package burnCalories.diet.controller;

import burnCalories.diet.DTO.userDTO.ResponseDurationLogDTO;
import burnCalories.diet.DTO.userDTO.ResponseUserInfoDTO;
import burnCalories.diet.DTO.userDTO.UpdateUserInfoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import burnCalories.diet.DTO.userDTO.ExerciseLogDTO;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;
import burnCalories.diet.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

    //운동 기록하기
    @PostMapping("/log")
    public ResponseEntity<String> putExerciseLog(@AuthenticationPrincipal String username, @RequestBody @Valid ExerciseLogDTO exerciseLog) {
        try {
            userService.validateDate(exerciseLog);


            userService.putExerciseLog(username, exerciseLog);
            return ResponseEntity.ok("운동 기록 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //누적 운동량
    @GetMapping("/cumulative")
    public ResponseEntity<List<?>> getWeeksExercise(@AuthenticationPrincipal String username, @RequestParam(required = false) int weeks, @RequestParam String type) {
        List<?> result = new ArrayList<>();
        if (type.equals("duration")) {
            result = userService.getDurationLog(weeks,username);
        } else if (type.equals("calories")) {
            result = userService.getCaloriesLog(weeks,username);
        }
        return ResponseEntity.ok(result);
    }

}